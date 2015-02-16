package uk.ac.ebi.spot.goci.model;

import org.apache.solr.client.solrj.beans.Field;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

/**
 * Javadocs go here!
 *
 * @author Tony Burdett
 * @date 16/01/15
 */
public class AssociationDocument extends EmbeddableDocument<Association> {
    // basic Association information
    @Field private String riskFrequency;
    @Field private String qualifier;

    @Field private float pValue;
    @Field private float orPerCopyNum;
    @Field private String orPerCopyUnitDescr;
    @Field private String orPerCopyRange;
    @Field private String orType;

    // additional included genetic data...
    // capture loci/risk alleles for association;
    // if many, collapse risk allele and snp into a single field and use
    // 'x' or ',' to separate SNP x SNP and haplotype associations respectively
    @Field private String rsId;
    @Field private String strongestAllele;
    @Field private String context;
    @Field private String region;
    // mapped genes and reported genes must be per snp -
    // if multiple, separate mapped genes with a hyphen (downstream-upstream) and reported genes with a slash,
    // and then include 'x' or ',' as designated by multuple loci/risk alleles
    @Field("mappedGene") private String mappedGene;
    @Field("reportedGene") private Collection<String> reportedGenes;

    // pluralise all other information, but retain order
    @Field("chromosomeName") private Set<String> chromosomeNames;
    @Field("chromosomePosition") private Set<Integer> chromosomePositions;
    @Field("last_modified") private Set<String> lastModifiedDates;

    public AssociationDocument(Association association) {
        super(association);
        this.riskFrequency = association.getRiskFrequency();
        this.qualifier = association.getPvalueText();
        this.orPerCopyUnitDescr = association.getOrPerCopyUnitDescr();
        this.orType = String.valueOf(association.getOrType());
        this.orPerCopyRange = association.getOrPerCopyRange();

        if (association.getOrPerCopyNum() != null) {
            this.orPerCopyNum = association.getOrPerCopyNum();
        }
        if (association.getPvalueFloat() != null) {
            this.pValue = association.getPvalueFloat();
        }

        this.chromosomeNames = new HashSet<>();
        this.chromosomePositions = new HashSet<>();
        this.lastModifiedDates = new HashSet<>();

        this.reportedGenes = new HashSet<>();
        embedGeneticData(association);
    }

    public String getRegion() {
        return region;
    }

    public String getMappedGene() {
        return mappedGene;
    }

    public String getStrongestAllele() {
        return strongestAllele;
    }

    public String getRiskFrequency() {
        return riskFrequency;
    }

    public String getQualifier() {
        return qualifier;
    }

    public String getOrPerCopyUnitDescr() {
        return orPerCopyUnitDescr;
    }

    public String getOrPerCopyRange() {
        return orPerCopyRange;
    }

    public String getContext() {
        return context;
    }

    public float getpValue() {
        return pValue;
    }

    public Collection<String> getReportedGenes() {
        return reportedGenes;
    }

    public String getRsId() {
        return rsId;
    }

    public Set<String> getChromosomeNames() {
        return chromosomeNames;
    }

    public Set<Integer> getChromosomePositions() {
        return chromosomePositions;
    }

    public Set<String> getLastModifiedDates() {
        return lastModifiedDates;
    }

    public float getOrPerCopyNum() {
        return orPerCopyNum;
    }

    public String getOrType() {
        return orType;
    }

    private void embedGeneticData(Association association) {
        if (association.getLoci().size() > 1) {
            // if this association has multiple loci, this is a SNP x SNP study
            association.getLoci().forEach(
                    locus -> {
                        locus.getStrongestRiskAlleles().forEach(
                                riskAllele -> {
                                    strongestAllele =
                                            setOrAppend(strongestAllele, riskAllele.getRiskAlleleName(), " x ");

                                    SingleNucleotidePolymorphism snp = riskAllele.getSnp();
                                    rsId = setOrAppend(rsId, snp.getRsId(), " x ");

                                    final Set<String> regionNames = new HashSet<>();
                                    final StringBuilder regionBuilder = new StringBuilder();
                                    snp.getRegions().forEach(
                                            region -> {
                                                if (!regionNames.contains(region.getName())) {
                                                    regionNames.add(region.getName());
                                                    setOrAppend(regionBuilder, region.getName(), " / ");
                                                }
                                            });
                                    region = setOrAppend(region, regionBuilder.toString(), " : ");
                                    mappedGene = setOrAppend(mappedGene, getMappedGeneString(association, snp), " : ");

                                    context = snp.getFunctionalClass();
                                    chromosomeNames.add(snp.getChromosomeName());
                                    if (snp.getChromosomePosition() != null) {
                                        chromosomePositions.add(Integer.parseInt(snp.getChromosomePosition()));
                                    }
                                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                                    df.setTimeZone(TimeZone.getTimeZone("UTC"));
                                    if (snp.getLastUpdateDate() != null) {
                                        lastModifiedDates.add(df.format(snp.getLastUpdateDate()));
                                    }
                                }
                        );
                        locus.getAuthorReportedGenes().forEach(gene -> reportedGenes.add(gene.getGeneName()));
                    }
            );
        }
        else {
            // this is a single study or a haplotype
            association.getLoci().forEach(
                    locus -> {
                        locus.getStrongestRiskAlleles().forEach(
                                riskAllele -> {
                                    strongestAllele =
                                            setOrAppend(strongestAllele, riskAllele.getRiskAlleleName(), ", ");

                                    SingleNucleotidePolymorphism snp = riskAllele.getSnp();
                                    rsId = setOrAppend(rsId, snp.getRsId(), ", ");

                                    final Set<String> regionNames = new HashSet<>();
                                    final StringBuilder regionBuilder = new StringBuilder();
                                    snp.getRegions().forEach(
                                            region -> {
                                                if (!regionNames.contains(region.getName())) {
                                                    regionNames.add(region.getName());
                                                    setOrAppend(regionBuilder, region.getName(), " / ");
                                                }
                                            });
                                    region = setOrAppend(region, regionBuilder.toString(), ", ");
                                    mappedGene = setOrAppend(mappedGene, getMappedGeneString(association, snp), ", ");

                                    context = snp.getFunctionalClass();
                                    chromosomeNames.add(snp.getChromosomeName());
                                    if (snp.getChromosomePosition() != null) {
                                        chromosomePositions.add(Integer.parseInt(snp.getChromosomePosition()));
                                    }
                                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                                    df.setTimeZone(TimeZone.getTimeZone("UTC"));
                                    if (snp.getLastUpdateDate() != null) {
                                        lastModifiedDates.add(df.format(snp.getLastUpdateDate()));
                                    }
                                }
                        );
                        locus.getAuthorReportedGenes().forEach(gene -> reportedGenes.add(gene.getGeneName()));
                    }
            );
        }
    }

    private String getMappedGeneString(Association association, SingleNucleotidePolymorphism snp) {
        List<String> genes = new ArrayList<>();
        snp.getGenomicContexts().forEach(
                context -> {
                    if (context.isDownstream()) {
                        genes.add(0, context.getGene().getGeneName());
                    }
                    else {
                        genes.add(context.getGene().getGeneName());
                    }
                });
        String geneString = "";
        if (genes.size() > 2) {
            throw new SolrIndexingException(
                    "Unable to index genetic data for association " +
                            "'" + association.getId() + ": more than 2 mapped genes " +
                            "(" + genes + ")");
        }
        else {
            if (genes.size() == 2) {
                geneString = genes.get(0).concat(" - ").concat(genes.get(1));
            }
            else {
                if (!genes.isEmpty()) {
                    geneString = genes.iterator().next();
                }
            }
        }
        return geneString;
    }

    private String setOrAppend(String current, String toAppend, String delim) {
        if (toAppend != null && !toAppend.isEmpty()) {
            if (current == null || current.isEmpty()) {
                current = toAppend;
            }
            else {
                current = current.concat(delim).concat(toAppend);
            }
        }
        return current;
    }

    private StringBuilder setOrAppend(StringBuilder current, String toAppend, String delim) {
        if (toAppend != null && !toAppend.isEmpty()) {
            if (current.length() == 0) {
                current.append(toAppend);
            }
            else {
                current.append(delim).append(toAppend);
            }
        }
        return current;
    }
}
