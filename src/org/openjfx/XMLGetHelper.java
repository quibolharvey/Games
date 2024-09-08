package org.openjfx;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class XMLGetHelper {

    public static final String ROOT = "db/";

    public static List<XMLQuestions> getAllQuestions() {
        try {
            List<XMLQuestions> xmlQuestionsList = new ArrayList<>();

            File inputFile = new File(ROOT + "questions.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("questions");
            for (int i = 0; i < nList.getLength(); i++) {
                Node problemNode = nList.item(i);
                NodeList elementsList = problemNode.getChildNodes();
                for (int x=0; x<elementsList.getLength(); x++) {
                    Node entriesNode = elementsList.item(x);
                    if (entriesNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element item = (Element) entriesNode;
                        xmlQuestionsList.add(new XMLQuestions(item.getTextContent(), item.getAttribute("answer"), item.getAttribute("op1"), item.getAttribute("op2"),
                                item.getAttribute("op3"), item.getAttribute("op4"), item.getAttribute("img"), item.getAttribute("chapter"), item.getAttribute("explanation")));
                    }
                }
            }

            return xmlQuestionsList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<XMLQuestions> getAllQuestions(String chapter) {
        try {
            List<XMLQuestions> xmlQuestionsList = new ArrayList<>();

            File inputFile = new File(ROOT + "questions.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("questions");
            for (int i = 0; i < nList.getLength(); i++) {
                Node problemNode = nList.item(i);
                NodeList elementsList = problemNode.getChildNodes();
                for (int x=0; x<elementsList.getLength(); x++) {
                    Node entriesNode = elementsList.item(x);
                    if (entriesNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element item = (Element) entriesNode;
                        if (item.getAttribute("chapter").equals(chapter)) {
                            xmlQuestionsList.add(new XMLQuestions(item.getTextContent(), item.getAttribute("answer"), item.getAttribute("op1"), item.getAttribute("op2"),
                                    item.getAttribute("op3"), item.getAttribute("op4"), item.getAttribute("img"), item.getAttribute("chapter"), item.getAttribute("explanation")));
                        }
                    }
                }
            }

            return xmlQuestionsList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Chapters> getAllChapters() {
        try {
            List<Chapters> chaptersArrayList = new ArrayList<>();

            File inputFile = new File(ROOT + "chapters.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("chapters");
            for (int i = 0; i < nList.getLength(); i++) {
                Node problemNode = nList.item(i);
                NodeList elementsList = problemNode.getChildNodes();
                for (int x=0; x<elementsList.getLength(); x++) {
                    Node entriesNode = elementsList.item(x);
                    if (entriesNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element item = (Element) entriesNode;
                        chaptersArrayList.add(new Chapters(item.getTextContent()));
                    }
                }
            }

            return chaptersArrayList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean addQuestion(XMLQuestions questions) {
        try {

            File inputFile = new File(ROOT + "questions.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            Node entryNode = doc.getElementsByTagName("questions").item(0);

            Element item = doc.createElement("item");
            item.setAttribute("answer", questions.getAnswer());
            item.setAttribute("op1", questions.getOption1());
            item.setAttribute("op2", questions.getOption2());
            item.setAttribute("op3", questions.getOption3());
            item.setAttribute("op4", questions.getOption4());
            item.setAttribute("img", questions.getImg());
            item.setAttribute("chapter", questions.getChapter());
            item.setAttribute("explanation", questions.getExplanation());
            item.setTextContent(questions.getQuestion());
            entryNode.appendChild(item);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(doc);
            StreamResult streamResult = new StreamResult(new File(ROOT + "questions.xml"));
            transformer.transform(domSource, streamResult);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteQuestion(XMLQuestions tb) {
        try {
            File inputFile = new File(ROOT + "questions.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("questions");
            for (int i = 0; i < nList.getLength(); i++) {
                Node entryNode = nList.item(i);
                NodeList elementsList = entryNode.getChildNodes();
                int x=0;
                while (x<elementsList.getLength()) {
                    Node entriesNode = elementsList.item(x);
                    if (entriesNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element items = (Element) entriesNode;
                        if (items.getTextContent().equals(tb.getQuestion()) & items.getAttribute("answer").equals(tb.getAnswer())) {
                            entryNode.removeChild(entriesNode);
                        }
                    }
                    x++;
                }
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(doc);
//            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            StreamResult streamResult = new StreamResult(new File(ROOT + "questions.xml"));
            transformer.transform(domSource, streamResult);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Scores> getScores() {
        try {
            List<Scores> scoresList = new ArrayList<>();

            File inputFile = new File(ROOT + "scores.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("scores");
            for (int i = 0; i < nList.getLength(); i++) {
                Node entryNode = nList.item(i);
                NodeList elementsList = entryNode.getChildNodes();
                int x=0;
                while (x<elementsList.getLength()) {
                    Node entriesNode = elementsList.item(x);
                    if (entriesNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element items = (Element) entriesNode;
                        scoresList.add(new Scores(items.getTextContent(),
                                items.getAttribute("date")));
                    }
                    x++;
                }
            }
            return scoresList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean insertScore(String score) {
        try {
            File inputFile = new File(ROOT + "scores.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            Node entryNode = doc.getElementsByTagName("scores").item(0);

            SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy h:m a");

            Element item = doc.createElement("item");
            item.setAttribute("date", sdf.format(new Date()));
            item.setTextContent(score);
            entryNode.appendChild(item);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(doc);
            StreamResult streamResult = new StreamResult(new File(ROOT + "scores.xml"));
            transformer.transform(domSource, streamResult);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean addChapter(Chapters chapters) {
        try {

            File inputFile = new File(ROOT + "chapters.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            Node entryNode = doc.getElementsByTagName("chapters").item(0);

            Element item = doc.createElement("item");
            item.setTextContent(chapters.getChapter());
            entryNode.appendChild(item);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(doc);
            StreamResult streamResult = new StreamResult(new File(ROOT + "chapters.xml"));
            transformer.transform(domSource, streamResult);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteChapter(String chapters) {
        try {
            File inputFile = new File(ROOT + "chapters.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("chapters");
            for (int i = 0; i < nList.getLength(); i++) {
                Node entryNode = nList.item(i);
                NodeList elementsList = entryNode.getChildNodes();
                int x=0;
                while (x<elementsList.getLength()) {
                    Node entriesNode = elementsList.item(x);
                    if (entriesNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element items = (Element) entriesNode;
                        if (items.getTextContent().equals(chapters)) {
                            entryNode.removeChild(entriesNode);
                        }
                    }
                    x++;
                }
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(doc);
//            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            StreamResult streamResult = new StreamResult(new File(ROOT + "chapters.xml"));
            transformer.transform(domSource, streamResult);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getMaxTime() {
        try {
            String time = "";
            File inputFile = new File(ROOT + "time.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            NodeList nList = doc.getElementsByTagName("time");
            for (int i = 0; i < nList.getLength(); i++) {
                Node problemNode = nList.item(i);
                NodeList elementsList = problemNode.getChildNodes();
                for (int x=0; x<elementsList.getLength(); x++) {
                    Node entriesNode = elementsList.item(x);
                    if (entriesNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element item = (Element) entriesNode;
                        time = item.getTextContent();
                    }
                }
            }
            return time;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
