package uk.ac.ebi.fgpt.goci.pussycat.layout;


import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

/**
 * Created by IntelliJ IDEA.
 * User: dwelter
 * Date: 03/04/12
 * Time: 14:41
 * To change this template use File | Settings | File Templates.
 */
public class SVGBuilder {

    Document doc;
    Element svgRoot;
    String svgNS;


    public SVGBuilder(int width, int height){

        DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
        doc = impl.createDocument(svgNS, "svg", null);

        svgRoot = doc.getDocumentElement();
        buildHeader(width, height);

    }
    
    
    public void addElement(Element element){
        svgRoot.appendChild((Element)doc.importNode(element,true));
    }
    
    public void removeElement(Element element){
        svgRoot.removeChild(element);
    }

    public String getSVG(){

        Transformer transformer = null;
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        StreamResult result = new StreamResult(new StringWriter());
        DOMSource source = new DOMSource(doc);
        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        String xmlString = result.getWriter().toString();

        return xmlString;
    }

    public void buildHeader(int width, int height){
        svgRoot.setAttribute("id", "goci-svg");
        svgRoot.setAttribute("width", Integer.toString(width));
        svgRoot.setAttribute("height", Integer.toString(height));
        svgRoot.setAttribute("xmlns:svg", "http://www.w3.org/2000/svg");
        svgRoot.setAttribute("xmlns:sodipodi", "http://sodipodi.sourceforge.net/DTD/sodipodi-0.dtd");

        String viewBox = "0 0 " + width + " " + height;
        svgRoot.setAttribute("viewBox", viewBox);
        svgRoot.setAttribute("preserveAspectRatio", "xMinYMin slice");

  /*      svgRoot.removeAttribute("zoomAndPan");
        svgRoot.removeAttribute("contentScriptType");
        svgRoot.removeAttribute("contentStyleType");    */


        Element defs = doc.createElement("defs");
        defs.setAttribute("xmlns", svgNS);
        
        Element linGrad1 = doc.createElement("linearGradient");
        linGrad1.setAttribute("id", "blacktobg");
        linGrad1.setAttribute("x1", "0%");
        linGrad1.setAttribute("y1", "0%");
        linGrad1.setAttribute("x2", "0%");
        linGrad1.setAttribute("y2", "100%");

        Element stop1 = doc.createElement("stop");
        stop1.setAttribute("offset", "0%");
        stop1.setAttribute("style","stop-color:black;stop-opacity:1");

        Element stop2 = doc.createElement("stop");
        stop2.setAttribute("offset", "100%");
        stop2.setAttribute("style","stop-color:#fffaea;stop-opacity:1");

        linGrad1.appendChild(stop1);
        linGrad1.appendChild(stop2);
        
        Element linGrad2 = doc.createElement("linearGradient");
        linGrad2.setAttribute("id", "bgtoblack");
        linGrad2.setAttribute("x1", "0%");
        linGrad2.setAttribute("y1", "0%");
        linGrad2.setAttribute("x2", "0%");
        linGrad2.setAttribute("y2", "100%");

        Element stop3 = doc.createElement("stop");
        stop3.setAttribute("offset", "0%");
        stop3.setAttribute("style","stop-color:#fffaea;stop-opacity:1");

        Element stop4 = doc.createElement("stop");
        stop4.setAttribute("offset", "100%");
        stop4.setAttribute("style","stop-color:black;stop-opacity:1");

        linGrad2.appendChild(stop3);
        linGrad2.appendChild(stop4);

        defs.appendChild(linGrad1);
        defs.appendChild(linGrad2);
        svgRoot.appendChild(defs);
    }

}

