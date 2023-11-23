package it.poste.patrimonio.ms.resources;

import static javax.ws.rs.core.Response.noContent;
import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.poste.patrimonio.bl.service.PositionService;
import it.poste.patrimonio.db.model.Position;
import it.poste.patrimonio.itf.api.PositionApi;
import lombok.extern.slf4j.Slf4j;

/**
 * This class has receive all REST request and process it.
 */
@Api(value = "positions",
     description = "Positions REST API for handle Positions CRUD operations on positions collection.",
     tags = {"positions"})
@Path("/positions")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class PositionResource {

    /**
     * Service position.
     */
    private PositionService positionService;

    /**
     * Constructor.
     *
     * @param positionService the dao position.
     */
    @Inject
    public PositionResource(PositionService positionService) {
        this.positionService = positionService;
    }
    /**
     * Get all {@link Position} objects.
     *
     * @return A object {@link Response} with the information of result this method.
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation success."),
            @ApiResponse(code = 404, message = "Positions not found")
    })
    @GET
    public Response all() {
        log.info("List all Positions.");
        final List<PositionApi> positionFind = positionService.getAll();
        if (positionFind.isEmpty()) {
            return Response.accepted(new it.poste.patrimonio.itf.api.Response("Positions not found."))
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
        return Response.ok(positionFind).build();
    }

    /**
     * Get a {@link Position} by identifier.
     *
     * @param ndg the identifier.
     * @return A object {@link Response} with the information of result this method.
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation success."),
            @ApiResponse(code = 202, message = "Position not found")
    })
    @GET
    @Path("/{ndg}")
    public Response getOne(@ApiParam(value = "ndg") @PathParam("ndg") @NotNull final String ndg) {
    	log.info("Find the position by identifier : " + ndg);
    	Optional<PositionApi> positionOpt = positionService.getOne(ndg);
    	return positionOpt.isPresent()
    			? Response.ok(positionOpt.get()).build()
    					: Response.accepted(new it.poste.patrimonio.itf.api.Response("Position not found.")).build();
    }

    /**
     * Persis a {@link Position} object in a collection.
     *
     * @param position The object to persist.
     * @return A object {@link Response} with the information of result this method.
     */
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Operation success.")
    })
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response save(@ApiParam(value = "Position") @NotNull final PositionApi position){
        log.info("Persist a position in collection with the information: {}", position);
        positionService.save(position);
        return Response.status(Response.Status.CREATED).build();
    }

    /**
     * Update the information of a {@link Position}.
     *
     * @param ndg    The identifier.
     * @param position the position information.
     * @return A object {@link Response} with the information of result this method.
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation success.")
    })
    @PUT
    @Path("/{ndg}")
    public Response update(@ApiParam(value = "ndg") @PathParam("ndg") @NotNull final String ndg,
                           @ApiParam(value = "Position") @NotNull final PositionApi position) {
        log.info("Update the information of a position : {} ", position);
        positionService.update(position);
        return Response.ok().build();
    }

    /**
     * Delete a {@link Position} object.
      * @param ndg   the identifier.
     * @return  A object {@link Response} with the information of result this method.
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation success.")
    })
    @DELETE
    @Path("/{ndg}")
    public Response delete(@ApiParam(value = "ndg") @PathParam("ndg") @NotNull final String ndg) {
        log.info("Delete a position from collection with identifier: " + ndg);
        
       return positionService.delete(ndg)
    		   ? noContent().build()
    				   : status(NOT_FOUND.getStatusCode()).build();
    }
}
