package net.juniper.netconf.adapter;

import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Objects;

/**
 * 自定义 namespace 的生成逻辑
 *
 * @author zifangsky
 * @date 21/2/5
 * @since 1.0.0
 */
public class NamespaceXmlFactory extends XmlFactory {
    private final String defaultNamespace;
//    private final Map<String, String> prefix2Namespace;

    public NamespaceXmlFactory(String defaultNamespace/*, Map<String, String> prefix2Namespace*/) {
        this.defaultNamespace = Objects.requireNonNull(defaultNamespace);
//        this.prefix2Namespace = Objects.requireNonNull(prefix2Namespace);
    }

    @Override
    protected XMLStreamWriter _createXmlWriter(IOContext ctxt, Writer w) throws IOException {
        XMLStreamWriter writer = super._createXmlWriter(ctxt, w);
        try {
            writer.setDefaultNamespace(defaultNamespace);
//            for (Map.Entry<String, String> e : prefix2Namespace.entrySet()) {
//                writer.setPrefix(e.getKey(), e.getValue());
//            }
        } catch (XMLStreamException e) {
            StaxUtil.throwAsGenerationException(e, null);
        }
        return writer;
    }



}
