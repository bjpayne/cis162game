package game;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/*****************************************************************
Handle the events for the GUI.
@author Ben Payne
@version 4/4/2017.
******************************************************************/
class MarkdownParser {
    /*****************************************************************
    Parse a markdown file to HTML
    @param fileName the assets file to read
    @return String the parsed markdown file
    ******************************************************************/
    static String parse(final URI fileName) {
        try {
            Path path = Paths.get(fileName);

            String content = new String(Files.readAllBytes(path));

            return render(content);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    /*****************************************************************
    Parse a markdown file to HTML
    @param content the content to parse
    @return String the parsed markdown file
    ******************************************************************/
    static String parse(final String content) {
        return render(content);
    }

    /*****************************************************************
    Parse a markdown file to HTML
    @param content the content to render
    @return String the parsed markdown file
    ******************************************************************/
    private static String render(final String content) {
        try {
            Parser parser = Parser.builder().build();

            Node document = parser.parse(content);

            HtmlRenderer renderer = HtmlRenderer.builder().build();

            return renderer.render(document);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}
