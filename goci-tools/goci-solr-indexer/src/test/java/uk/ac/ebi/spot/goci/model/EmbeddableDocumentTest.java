
package uk.ac.ebi.spot.goci.model;

import org.junit.Before;
import org.junit.Test;
import uk.ac.ebi.spot.goci.exception.DocumentEmbeddingException;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Collections;
import java.util.Date;

import static org.junit.Assert.fail;

/**
 * Javadocs go here!
 *
 * @author Tony Burdett
 * @date 14/02/15
 */
public class EmbeddableDocumentTest {
    private Study study;
    private Association association;

    private StudyDocument studyDoc;
    private AssociationDocument associationDoc;

    @Before
    public void setUp() {
        Housekeeping h = new Housekeeping();
        h.setLastUpdateDate(new Date());
        h.setPublishDate(new Date());
        this.study = new Study("author", new Date(), "publication", "title", "initial sample size", "replicate " +
                "sample size", "platform", "123456", false, false, false, null, Collections.<EfoTrait>emptyList(),
                               Collections.<SingleNucleotidePolymorphism>emptyList(), h);
        study.setId(1l);
        this.studyDoc = new StudyDocument(study);

        this.association = new Association("riskFrequency",
                                           "allele",
                                           0.00000005f,
                                           "pValueText",
                                           1.0f,
                                           false,
                                           "snpType",
                                           false,
                                           false,
                                           true,
                                           1,
                                           1,
                                           1.0f,
                                           1.0f,
                                           "orPerCopyRange",
                                           "orPerCopyDescr",
                                           study,
                                           Collections.emptyList(),
                                           Collections.emptyList());
        association.setId(2l);
        this.associationDoc = new AssociationDocument(association);
    }

    @Test
    public void testEmbed() {
        try {
            studyDoc.embed(associationDoc);
        }
        catch (DocumentEmbeddingException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testIntrospection() {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(studyDoc.getClass());
            for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
                System.out.println("Display name: " + pd.getDisplayName());
                System.out.println("Name: " + pd.getName());
                System.out.println("Read method: " + pd.getReadMethod());
                System.out.println("\t" + pd);
            }

        }
        catch (IntrospectionException e) {
            e.printStackTrace();
            fail();
        }
    }
}
