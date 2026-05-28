package com.automifychat.app.markdown;

import org.commonmark.Extension;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MarkdownRenderer {

    private final Parser parser;
    private final HtmlRenderer renderer;
    private final PolicyFactory sanitizer;

    public MarkdownRenderer() {
        List<Extension> extensions = List.of(
                TablesExtension.create(),
                StrikethroughExtension.create(),
                AutolinkExtension.create()
        );
        this.parser = Parser.builder().extensions(extensions).build();
        this.renderer = HtmlRenderer.builder().extensions(extensions).build();
        this.sanitizer = new HtmlPolicyBuilder()
                .allowElements("p", "br", "strong", "em", "code", "pre", "blockquote",
                        "ul", "ol", "li", "h1", "h2", "h3", "h4", "h5", "h6",
                        "a", "del", "table", "thead", "tbody", "tr", "th", "td",
                        "img", "hr")
                .allowAttributes("href").onElements("a")
                .allowAttributes("class").onElements("code", "pre")
                .allowAttributes("src", "alt", "title").onElements("img")
                .allowUrlProtocols("http", "https", "mailto")
                .requireRelNofollowOnLinks()
                .toFactory();
    }

    public String render(String markdown) {
        if (markdown == null || markdown.isBlank()) return "";
        String unsafeHtml = renderer.render(parser.parse(markdown));
        return sanitizer.sanitize(unsafeHtml);
    }
}
