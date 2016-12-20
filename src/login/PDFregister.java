/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/**
 *
 * @author tim
 */
public class PDFregister {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Mysql mysql = new Mysql(); 
        // Create a new empty document
        PDDocument document = new PDDocument();

        // Create a new blank page and add it to the document
        PDPage blankPage = new PDPage();
        document.addPage( blankPage );

        try {
            PDFont font = PDType1Font.HELVETICA_BOLD;
            PDFont font2 = PDType1Font.TIMES_ROMAN;

            // Start a new content stream which will "hold" the to be created content
            PDPageContentStream contentStream = new PDPageContentStream(document, blankPage);
            // headline
            contentStream.beginText();
            contentStream.setFont( font, 15 );
            contentStream.moveTextPositionByAmount( 75, 700 );
            contentStream.drawString( "Vermiste bagage registratie formulier" );
            contentStream.endText();
            
            //Datum 
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 175, 650 );
            contentStream.drawString( "Datum: " );
            contentStream.endText();
            //tijd
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 187, 635 );
            contentStream.drawString( "Tijd: " );
            contentStream.endText();
            //luchthaven
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 150, 620 );
            contentStream.drawString( "Luchthaven: " );
            contentStream.endText();
            
            //begin reiziger informatie
            contentStream.beginText();
            contentStream.setFont( font, 12 );
            contentStream.moveTextPositionByAmount( 100, 590 );
            contentStream.drawString( "Reiziger informatie: " );
            contentStream.endText();
            //Naam
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 180, 575 );
            contentStream.drawString( "Naam: " );
            contentStream.endText();
            //Adres
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 180, 560 );
            contentStream.drawString( "Adres: " );
            contentStream.endText();
            //Woonplaats
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 152, 545 );
            contentStream.drawString( "Woonplaats: " );
            contentStream.endText();
            //Postcode
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 165, 530 );
            contentStream.drawString( "Postcode: " );
            contentStream.endText();
            //Land
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 185, 515 );
            contentStream.drawString( "Land: " );
            contentStream.endText();
            //Telefoon
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 167, 500 );
            contentStream.drawString( "Telefoon: " );
            contentStream.endText();
            //E-mail
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 178, 485 );
            contentStream.drawString( "E-mail: " );
            contentStream.endText();
            
            //bagagelabel informatie
            contentStream.beginText();
            contentStream.setFont( font, 12 );
            contentStream.moveTextPositionByAmount( 100, 455 );
            contentStream.drawString( "Bagage label informatie: " );
            contentStream.endText();
            //Label nummer
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 140, 440 );
            contentStream.drawString( "Label nummer: " );
            contentStream.endText();
            //Vluchtnummer
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 138, 425 );
            contentStream.drawString( "Vluchtnummer: " );
            contentStream.endText();
            //Bestemming
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 150, 410 );
            contentStream.drawString( "Bestemming: " );
            contentStream.endText();
            
            //Bagage informatie
            contentStream.beginText();
            contentStream.setFont( font, 12 );
            contentStream.moveTextPositionByAmount( 100, 380 );
            contentStream.drawString( "Bagage informatie: " );
            contentStream.endText();
            //Type
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 187, 365 );
            contentStream.drawString( "Type: " );
            contentStream.endText();
            //Merk
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 185, 350);
            contentStream.drawString( "Merk: " );
            contentStream.endText();
            //Kleur
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 184, 335 );
            contentStream.drawString( "Kleur: " );
            contentStream.endText();
            //Kenmerken
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 155, 320 );
            contentStream.drawString( "Kenmerken: " );
            contentStream.endText();
            
            //handtekening reiziger
            contentStream.beginText();
            contentStream.setFont( font, 12 );
            contentStream.moveTextPositionByAmount( 100, 220 );
            contentStream.drawString( "Handtekening reiziger: " );
            contentStream.endText();
            //Handtekening klantenservice
            contentStream.beginText();
            contentStream.setFont( font, 12 );
            contentStream.moveTextPositionByAmount( 100, 140 );
            contentStream.drawString( "Handtekening klantenservice: " );
            contentStream.endText();
            
            //input Datum
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 215, 650 );
            contentStream.drawString( "Teks" );
            contentStream.endText();
            //input tijd
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 215, 635 );
            contentStream.drawString( "Teks" );
            contentStream.endText();
            //input luchthaven
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 215, 620 );
            contentStream.drawString( "Teks" );
            contentStream.endText();
            //input Naam
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 215, 575 );
            contentStream.drawString( "Teks" );
            contentStream.endText();
            //input Adres
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 215, 560 );
            contentStream.drawString( "Teks" );
            contentStream.endText();
            //input Woonplaats
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 215, 545 );
            contentStream.drawString( "Teks" );
            contentStream.endText();
            //input Postcode
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 215, 530 );
            contentStream.drawString( "Teks" );
            contentStream.endText();
            //input Land
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 215, 515 );
            contentStream.drawString( "Teks" );
            contentStream.endText();
            //input Telefoon
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 215, 500 );
            contentStream.drawString( "Teks" );
            contentStream.endText();
            //input E-mail
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 215, 485 );
            contentStream.drawString( "Teks" );
            contentStream.endText();
            //input Label nummer
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 215, 440 );
            contentStream.drawString( "Teks" );
            contentStream.endText();
            //input Vluchtnummer
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 215, 425 );
            contentStream.drawString( "Teks" );
            contentStream.endText();
            //input Bestemming
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 215, 410 );
            contentStream.drawString( "Teks" );
            contentStream.endText();
            //input Type
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 215, 365 );
            contentStream.drawString( "Teks" );
            contentStream.endText();
            //input Merk
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 215, 350 );
            contentStream.drawString( "Teks" );
            contentStream.endText();
            //input Kleur
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 215, 335 );
            contentStream.drawString( "Teks" );
            contentStream.endText();
            //input Kenmerken
            contentStream.beginText();
            contentStream.setFont( font2, 12 );
            contentStream.moveTextPositionByAmount( 215, 320 );
            contentStream.drawString( "Teks" );
            contentStream.endText();
            
            
            
            


            // Make sure that the content stream is closed:
            contentStream.close();

            document.save("Gevonden bagage.pdf");
            document.close();
        } catch (IOException ex) {
            Logger.getLogger(PDFregister.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
