package com.bricklink.fulfillment.configuration;

import com.bricklink.api.rest.client.BricklinkRestClient;
import com.bricklink.fulfillment.api.ReferenceService;
import com.bricklink.fulfillment.api.bricklink.BricklinkOrderService;
import com.bricklink.fulfillment.api.shipstation.OrdersAPI;
import com.bricklink.fulfillment.mapper.BricklinkToShipstationMapper;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.vattima.lego.imaging.service.AlbumManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bricklink.data.lego.dao.BricklinkInventoryDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class BricklinkConfiguration {
    private final BricklinkRestClient bricklinkRestClient;
    private final OrdersAPI ordersAPI;
    private final AlbumManager albumManager;
    private final BricklinkInventoryDao bricklinkInventoryDao;
    private final PhotosInterface photosInterface;
    private final ReferenceService referenceService;

    @Bean
    public BricklinkOrderService bricklinkOrderService() {
        return new BricklinkOrderService(bricklinkRestClient, referenceService);
    }

    @Bean
    public BricklinkToShipstationMapper bricklinkToShipstationMapper(BricklinkOrderService bricklinkOrderService, ReferenceService referenceService) {
        return new BricklinkToShipstationMapper(bricklinkInventoryDao, albumManager, photosInterface, bricklinkOrderService, referenceService);
    }
}
