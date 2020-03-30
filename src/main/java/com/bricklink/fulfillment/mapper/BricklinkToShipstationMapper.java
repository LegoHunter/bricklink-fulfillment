package com.bricklink.fulfillment.mapper;

import com.bricklink.api.rest.model.v1.Cost;
import com.bricklink.api.rest.model.v1.Item;
import com.bricklink.api.rest.model.v1.Order;
import com.bricklink.api.rest.model.v1.Payment;
import com.bricklink.fulfillment.BricklinkFulfillmentException;
import com.bricklink.fulfillment.api.ReferenceService;
import com.bricklink.fulfillment.api.bricklink.BricklinkOrderService;
import com.bricklink.fulfillment.shipstation.model.Address;
import com.bricklink.fulfillment.shipstation.model.Country;
import com.bricklink.fulfillment.shipstation.model.OrderItem;
import com.bricklink.fulfillment.shipstation.model.ShipStationOrder;
import com.bricklink.fulfillment.shipstation.model.Weight;
import com.bricklink.util.DateUtils;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.vattima.lego.imaging.model.AlbumManifest;
import com.vattima.lego.imaging.service.AlbumManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bricklink.data.lego.dao.BricklinkInventoryDao;
import net.bricklink.data.lego.dto.BricklinkInventory;
import org.apache.commons.text.StringEscapeUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.Normalizer;
import java.util.Date;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static com.bricklink.fulfillment.shipstation.model.OrderStatus.AWAITING_PAYMENT;

@Slf4j
@RequiredArgsConstructor
public class BricklinkToShipstationMapper {
    public final Function<String, String> accentStripper = s -> s == null ? null :
            Normalizer.normalize(s, Normalizer.Form.NFD)
                      .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    private final BricklinkInventoryDao bricklinkInventoryDao;
    private final AlbumManager albumManager;
    private final PhotosInterface photosInterface;
    private final BricklinkOrderService bricklinkOrderService;
    private final ReferenceService referenceService;
    public Function<com.bricklink.api.rest.model.v1.Address, Address> addressMapper = ba -> {
        log.debug("Mapping Bricklink Address [{}] to ShipStation", ba);
        Address shipStationAddress = new Address();
        shipStationAddress.setName(accentStripper.apply(ba
                .getName()
                .getFull()));
        shipStationAddress.setStreet1(accentStripper.apply(ba.getAddress1()));
        shipStationAddress.setStreet2(accentStripper.apply(ba.getAddress2()));
        shipStationAddress.setStreet3("");
        shipStationAddress.setCity(accentStripper.apply(ba.getCity()));
        shipStationAddress.setState(accentStripper.apply(ba.getState()));
        shipStationAddress.setPostalCode(ba.getPostal_code());
        shipStationAddress.setCountry(lookupCountry(ba.getCountry_code()).getCode());
        return shipStationAddress;
    };

    public BiConsumer<Order, ShipStationOrder> bricklinkToShipstationOrderMapper = (bricklinkOrder, shipStationOrder) -> {
        log.debug("Mapping Bricklink Order [{}] to ShipStation", bricklinkOrder);
        shipStationOrder.setOrderNumber(String.format("BL-%s", bricklinkOrder.getOrder_id()));
        shipStationOrder.setOrderKey(shipStationOrder.getOrderNumber());
        shipStationOrder.setOrderDate(DateUtils.toDate(bricklinkOrder.getDate_ordered()));
        shipStationOrder.setOrderStatus(AWAITING_PAYMENT.label);
        shipStationOrder.setCustomerUsername(bricklinkOrder.getBuyer_name());
        shipStationOrder.setCustomerEmail(bricklinkOrder.getBuyer_email());
        shipStationOrder.setBillTo(addressMapper.apply(bricklinkOrder.getShipping()
                                                                     .getAddress()));
        shipStationOrder.setShipTo(shipStationOrder.getBillTo());
        shipStationOrder.setCarrierCode("stamps_com");
        shipStationOrder.setServiceCode(getService(bricklinkOrder));
        shipStationOrder.setOrderTotal(bricklinkOrder.getCost()
                                                     .getGrand_total());
        Cost cost = bricklinkOrder.getCost();
        Double shippingAmount = cost.getShipping() + cost.getInsurance();
        shipStationOrder.setShippingAmount(shippingAmount);
        shipStationOrder.setInternalNotes(bricklinkOrder.getRemarks());

        if (bricklinkOrder.isPaid()) {
            Payment payment = bricklinkOrder.getPayment();
            Date paymentDate = DateUtils.toDate(payment.getDate_paid());
            Double grandTotal = cost.getGrand_total();
            log.info("\tBricklink Order is Paid, updating ShipStation order date [{}] and total payment [{}]", paymentDate, grandTotal);
            shipStationOrder.updateStatusToPaid(paymentDate, grandTotal);
        }
    };

    public Function<com.bricklink.api.rest.model.v1.OrderItem, OrderItem> bricklinkToShipstationOrderItemMapper = bricklinkOrderItem -> {
        Item item = bricklinkOrderItem.getItem();
        OrderItem shipStationOrderItem = new OrderItem();
        shipStationOrderItem.setLineItemKey(null);
        shipStationOrderItem.setName(String.format("(%s) - %s", item.getNo(), StringEscapeUtils.unescapeHtml4(item.getName())));

        Weight weight = new Weight();
        weight.setUnits("grams");
        weight.setValue((int) Math.round(bricklinkOrderItem.getWeight()));
        shipStationOrderItem.setWeight(weight);
        shipStationOrderItem.setQuantity(bricklinkOrderItem.getQuantity());
        shipStationOrderItem.setUnitPrice(bricklinkOrderItem.getUnit_price());
        shipStationOrderItem.setUpc(bricklinkOrderItem.getItem()
                                                      .getNo());
        log.debug("Mapping Bricklink OrderItem [{}] to ShipStation OrderItem [{}]", bricklinkOrderItem, shipStationOrderItem);
        return shipStationOrderItem;
    };

    public void mapBricklinkOrderToShipStationOrder(Order bricklinkOrder, ShipStationOrder shipStationOrder) {
        bricklinkToShipstationOrderMapper.accept(bricklinkOrder, shipStationOrder);
    }

    public OrderItem addOrderItem(com.bricklink.api.rest.model.v1.OrderItem bricklinkOrderItem, ShipStationOrder shipStationOrder) {

        // Find Bricklink Inventory from database using Inventory Id.
        Long inventoryId = bricklinkOrderItem.getInventory_id();
        BricklinkInventory bricklinkInventory = bricklinkInventoryDao.getByInventoryId(inventoryId)
                                                                     .orElseThrow(() -> new BricklinkFulfillmentException(String.format("Unable to find inventory_id [%s] in bricklink_inventory table", inventoryId)));

        OrderItem shipStationOrderitem = bricklinkToShipstationOrderItemMapper.apply(bricklinkOrderItem);

        // Set the OrderItem image URL from Flickr
        AlbumManifest albumManifest = albumManager.getAlbumManifest(bricklinkInventory.getUuid(), bricklinkInventory.getBlItemNo());
        try {
            Photo photo = photosInterface.getPhoto(albumManifest.getPrimaryPhoto()
                                                                .getPhotoId());
            try {
                String photoUrl = new URL(photo.getSquareLargeUrl()).toExternalForm();
                log.debug("Setting ShipStation OrderItem url to [{}]", photoUrl);
                shipStationOrderitem.setImageUrl(photoUrl);
            } catch (MalformedURLException e) {
                shipStationOrderitem.setImageUrl(null);
            }
        } catch (FlickrException e) {
            throw new BricklinkFulfillmentException(e);
        }

        // Set remaining fields
        shipStationOrderitem.setSku(bricklinkInventory.getUuid());
        shipStationOrderitem.setWarehouseLocation(String.format("%02d-%03d", bricklinkInventory.getBoxId(), bricklinkInventory.getBoxIndex()));
        shipStationOrder.getShipStationOrderItems()
                        .add(shipStationOrderitem);
        return shipStationOrderitem;
    }

    private Country lookupCountry(String code) {
        return referenceService.lookupCountry(code);
    }

    private String getService(Order bricklinkOrder) {
        return bricklinkOrderService.getService(bricklinkOrder);
    }
}
