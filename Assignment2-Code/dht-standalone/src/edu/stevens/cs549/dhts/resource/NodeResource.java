package edu.stevens.cs549.dhts.resource;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.logging.Logger;

@Path("/dht")
public class NodeResource {

	/*
	 * Web service API.
	 * 
	 * TODO: Fill in the missing operations.
	 */

	Logger log = Logger.getLogger(NodeResource.class.getCanonicalName());

	@Context
	UriInfo uriInfo;

	@Context
	HttpHeaders headers;

	@GET
	@Path("info")
	@Produces("application/xml")
	public Response getNodeInfoXML() {
		return new NodeService(headers, uriInfo).getNodeInfo();
	}


	@GET
	@Path("info")
	@Produces("application/json")
	public Response getNodeInfoJSON() {
		return new NodeService(headers, uriInfo).getNodeInfo();
	}
    
	@GET
	@Path("pred")
	@Produces("application/xml")
	public Response getPred() {
		return new NodeService(headers, uriInfo).getPred();
	}
	
	/*
	 * Actually returns a TableRep (annotated with @XmlRootElement)
	 */
	@PUT
	@Path("notify")
	@Consumes("application/xml")
	@Produces("application/xml")
	/*
	 * Actually returns a TableRep (annotated with @XmlRootElement)
	 */
	public Response putNotify(TableRep predDb) {
		/*
		 * See the comment for WebClient::notify (the client side of this logic).
		 */
		return new NodeService(headers, uriInfo).notify(predDb);
		// NodeInfo p = predDb.getInfo();
	}

	@GET
	@Path("find")
	@Produces("application/xml")
	public Response findSuccessor(@QueryParam("id") String index) {
		int id = Integer.parseInt(index);
		return new NodeService(headers, uriInfo).findSuccessor(id);
	}

	@GET
	@Path("succ")
	@Produces("application/xml")
	public Response getSucc(){
		return new NodeService(headers, uriInfo).getSucc();
	}
	
	@GET
	@Path("finger")
	@Produces("application/xml")
	public Response findClosestPrecedingFinger(@QueryParam("id") String index) {
		int id = Integer.parseInt(index);
		return new NodeService(headers, uriInfo).findClosestPrecedingFinger(id);
	}
	
	/*
	 * --------------------------------- Key Operation
	 */
	
	@GET
	@Path("{key}")
	@Produces("application/xml")
	public Response get(@PathParam("key") String key) {
		return new NodeService(headers, uriInfo).get(key);
	}

	@PUT
	@Path("{key}/{value}")
	@Produces("application/xml")
	public Response add(@PathParam("key") String key, @PathParam("value") String value) {
		return new NodeService(headers, uriInfo).add(key,value);
	}

	@DELETE
	@Path("{key}/{value}")
	@Produces("application/xml")
	public Response delete(@PathParam("key") String key, @PathParam("value") String value) {
		return new NodeService(headers, uriInfo).delete(key, value);
	}
}