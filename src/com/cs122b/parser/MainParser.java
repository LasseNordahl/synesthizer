package com.cs122b.parser;

import java.io.IOException;
import java.sql.SQLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MainParser {
    String fileName;
    Document dom;

    ArtistParser artistParser;
    AlbumParser albumParser;
    TrackParser trackParser;
    TrackMetaParser trackMetaParser;

    public MainParser(String file) {
        fileName = file;
        artistParser = new ArtistParser();
        albumParser = new AlbumParser();
        trackParser = new TrackParser();
        trackMetaParser = new TrackMetaParser();
    }

    public void run() {
        // parse the xml file and get the dom object
        parseXmlFile();

        System.out.println("we are going to parse the Doc");
        // get each employee element and create a Employee object
        parseDocument();
    }

    private void parseXmlFile() {
        // get the factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            dom = db.parse(fileName);
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void parseDocument() {
        // get the root elememt
        Element docEle = dom.getDocumentElement();

        NodeList artists = docEle.getElementsByTagName("artists");
        if (artists != null && artists.getLength() > 0) {
            System.out.println("Found artists");
            // get artists element
            Element artistsElem = (Element) artists.item(0);
            artistParser.parseArtists(artistsElem);
            try {
                artistParser.commitArtists();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        NodeList albums = docEle.getElementsByTagName("albums");
        if (albums != null && albums.getLength() > 0) {
            System.out.println("Found albums");
            // get artists element
            Element albumsElem = (Element) albums.item(0);
            albumParser.parseAlbums(albumsElem);
        }

        NodeList tracks = docEle.getElementsByTagName("tracks");
        if (tracks != null && tracks.getLength() > 0) {
            System.out.println("Found tracks");
            // get artists element
            Element tracksElem = (Element) tracks.item(0);
            trackParser.parseTracks(tracksElem);
        }

        NodeList tracks_meta = docEle.getElementsByTagName("tracks_meta");
        if (tracks_meta != null && tracks_meta.getLength() > 0) {
            System.out.println("Found track_meta");
            // get artists element
            Element tracksMetaElem = (Element) tracks_meta.item(0);
            trackMetaParser.parseTrackMetas(tracksMetaElem);
        }
    }

    public static void main(String[] args) {
        System.out.println("We are starting");
        MainParser dpe = new MainParser(
                "/Users/zacharypinto/Documents/UCI_Classes/cs122b/cs122b-spring20-team-53/data_collection_creation/xml_creation/artists.xml");
        dpe.run();
    }
}