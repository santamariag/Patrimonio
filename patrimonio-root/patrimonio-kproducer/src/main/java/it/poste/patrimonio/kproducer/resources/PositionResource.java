package it.poste.patrimonio.kproducer.resources;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.poste.patrimonio.db.model.Position;
import it.poste.patrimonio.itf.api.PositionApi;
import it.poste.patrimonio.kproducer.core.KafkaMessageProducer;
import lombok.extern.slf4j.Slf4j;

/**
 * This class has receive all REST request and process it.
 */
@Api(value = "positions",
     description = "Utility Positions REST API to gather postion info for the kafka producer to send.",
     tags = {"positions"})
@Path("/positions")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class PositionResource {

   
	/**
    * KProducer.
    */
    private KafkaMessageProducer producer;

    /**
     * Constructor.
     *
     * @param producer the kafka producer.
     */
    public PositionResource(KafkaMessageProducer producer) {
        this.producer = producer;
    }
    
    /**
     * Publish a {@link Position} object in a kafka topic.
     *
     * @param position The object to publish.
     * @return A object {@link Response} with the information of result this method.
     */
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Operation success.")
    })
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response save(@ApiParam(value = "Position") @NotNull final PositionApi position){
        log.info("Publish the position: {}", position);
        producer.send(position);
        return Response.status(Response.Status.CREATED).build();
    }

}
