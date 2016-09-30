package it.smartcommunitylab.mobilityservice.services.service.parcheggi;

import it.smartcommunitylab.mobilityservice.services.MobilityService;
import it.smartcommunitylab.mobilityservice.services.MobilityServiceException;
import it.smartcommunitylab.mobilityservice.services.MobilityServiceObject;
import it.smartcommunitylab.mobilityservice.services.service.parcheggi.model.Parcheggio;

import java.util.List;

import org.springframework.stereotype.Component;

import redstone.xmlrpc.XmlRpcArray;
import redstone.xmlrpc.XmlRpcClient;

import com.google.common.collect.Lists;

@Component
public class ParcheggiRoveretoService extends MobilityService {

	@Override
	protected List<MobilityServiceObject> invokeService() throws MobilityServiceException {
		try {
			List<MobilityServiceObject> result = Lists.newArrayList();
			
			Object res;
			XmlRpcClient client = new XmlRpcClient("http://88.51.119.253:8070/RPC2", true);
			res = client.invoke("pGuide.getElencoIdentificativiParcheggi", new Object[0]);

			for (Object o : (XmlRpcArray) res) {
				try {
				XmlRpcArray result2;
				Parcheggio parcheggio = new Parcheggio();

				result2 = (XmlRpcArray)client.invoke("pGuide.getCaratteristicheParcheggio", new Object[] { (Integer)o });
				String id = result2.getString(1);
				String ids[] = id.split("-");
				String address = ids[ids.length - 1].trim();
				parcheggio.setId(address);
				parcheggio.setAddress(address);
				
				result2 = (XmlRpcArray) client.invoke("pGuide.getPostiLiberiParcheggio", new Object[] { (Integer)o });
				parcheggio.setPlaces("" + result2.getInteger(1));
//				parcheggio.setTimestamp(System.currentTimeMillis());
				
				result.add(parcheggio);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			
			return result;
		} catch (Exception e) {
			throw new MobilityServiceException(e);
		}
	}

}
