/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.caib.sgtsic.utils.generacion;

import net.sf.jooreports.templates.DocumentTemplate;

/**
 * Transformaciones de texto para documentos generados
 * desde plantillas .odt.
 * @author abastos
 */
public class TransformadorOdt implements DocumentTemplate.ContentWrapper {

    /**
     * Transforma el texto según las reglas que definamos.
     *
     * - Transforma los saltos de línea (\n) en line breaks con un tabulador
     * que los precede (de ese modo se evita el problema de la justificación).
     * - Escapa entidades XML
     *
     * @param in Contenido de entrada
     * @return Contenido transformado
     */
    public String wrapContent(String in) {
        return "[#ftl]\n"
            + "[#escape any as any?xml?replace(\"\\n\",\"<text:tab/><text:line-break />\")]\n"
            + in
            + "[/#escape]";
    }
}
