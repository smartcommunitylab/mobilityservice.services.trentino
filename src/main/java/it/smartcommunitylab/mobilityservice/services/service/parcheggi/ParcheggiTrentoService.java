package it.smartcommunitylab.mobilityservice.services.service.parcheggi;

import it.smartcommunitylab.mobilityservice.services.MobilityService;
import it.smartcommunitylab.mobilityservice.services.MobilityServiceException;
import it.smartcommunitylab.mobilityservice.services.MobilityServiceObject;
import it.smartcommunitylab.mobilityservice.services.service.parcheggi.model.Parcheggio;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.google.common.collect.Lists;

@Component
public class ParcheggiTrentoService extends MobilityService {

	@Override
	protected List<MobilityServiceObject> invokeService() throws MobilityServiceException {
		try {
			InputStream is = connect();
			List<MobilityServiceObject> result = parse(is);
			return result;
		} catch (Exception e) {
			throw new MobilityServiceException(e);
		}
	}

	public List<MobilityServiceObject> parse(InputStream is) throws Exception {
		List<MobilityServiceObject> result = Lists.newArrayList();

		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = builder.parse(is);
		is.close();

		NodeList nodes1 = (NodeList) XPathFactory.newInstance().newXPath().compile("//GetDataItemsByFieldResult/SvcDataItem/fields").evaluate(doc.getDocumentElement(), XPathConstants.NODESET);
		for (int i = 0; i < nodes1.getLength(); i++) {
			Element element1 = (Element) nodes1.item(i);
			NodeList nodes2 = (NodeList) XPathFactory.newInstance().newXPath().compile("KeyValueOfstringanyType").evaluate(element1, XPathConstants.NODESET);
			Map<String, String> map = new TreeMap<String, String>();
			for (int j = 0; j < nodes2.getLength(); j++) {
				Element element2 = (Element) nodes2.item(j);
				NodeList nodes3 = element2.getChildNodes();
				String key = null;
				String value = null;
				for (int k = 0; k < nodes3.getLength(); k++) {
					if ("b:Key".equals(nodes3.item(k).getNodeName())) {
						key = nodes3.item(k).getTextContent();
					}
					if ("b:Value".equals(nodes3.item(k).getNodeName())) {
						value = nodes3.item(k).getTextContent();
					}
				}
				map.put(key, value);
			}
			Parcheggio pb = new Parcheggio();
			pb.setId(map.get("Identificativo"));
			pb.setAddress(map.get("Indirizzo"));
			pb.setPlaces(map.get("Posti Liberi"));

//			pb.setTimestamp(System.currentTimeMillis());
			result.add(pb);
		}

		return result;

	}

	private InputStream connect() throws Exception {
		InputStream fis = Thread.currentThread().getContextClassLoader().getResourceAsStream("soapenv.xml");
		BufferedInputStream bis = new BufferedInputStream(fis);

		StringBuffer sb = new StringBuffer();

		byte buffer[] = new byte[1000];
		int n;
		String b;
		try {
			while ((n = bis.read(buffer)) > -1) {
				b = new String(buffer, 0, n, "UTF-8");
				sb.append(b);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		bis.close();

		String soapXml = sb.toString();

		URL url = new URL("http://phylumprod/LinkService/LinkService.svc/Basic");
		URLConnection conn = null;

		conn = url.openConnection();
		conn.setRequestProperty("SOAPAction", "urn:ILinkService/GetDataItemsByField");
		// conn.setRequestProperty("Accept-Encoding", "gzip,deflate");
		conn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
		conn.setDoOutput(true);
		conn.setConnectTimeout(30000);
		conn.setReadTimeout(30000);
		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write(soapXml);
		wr.flush();
		wr.close();
		return conn.getInputStream();
	}

}
