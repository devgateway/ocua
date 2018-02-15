package org.devgateway.toolkit.persistence.mongo.spring;

import org.apache.commons.io.IOUtils;
import org.devgateway.ocds.persistence.mongo.DefaultLocation;
import org.devgateway.ocds.persistence.mongo.Organization;
import org.devgateway.ocds.persistence.mongo.Release;
import org.devgateway.ocds.persistence.mongo.constants.MongoConstants;
import org.devgateway.ocds.persistence.mongo.flags.FlagsConstants;
import org.slf4j.Logger;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ScriptOperations;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.TextIndexDefinition.TextIndexDefinitionBuilder;
import org.springframework.data.mongodb.core.script.ExecutableMongoScript;
import org.springframework.data.mongodb.core.script.NamedMongoScript;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;

import static org.devgateway.ocds.persistence.mongo.constants.MongoConstants.FieldNames.FLAGS_TOTAL_FLAGGED;

public abstract class AbstractMongoDatabaseConfiguration {

    protected abstract Logger getLogger();

    protected abstract MongoTemplate getTemplate();

    public void createMandatoryImportIndexes() {
        //mongoTemplate.indexOps(Release.class).ensureIndex(new Index().on("planning.budget.projectID", Direction.ASC));
        //mongoTemplate.indexOps(Location.class).ensureIndex(new Index().on("description", Direction.ASC));
        getTemplate().indexOps(Organization.class).ensureIndex(new Index().on("identifier._id", Direction.ASC));
        getTemplate().indexOps(Organization.class)
                .ensureIndex(new Index().on("additionalIdentifiers._id", Direction.ASC));
        getTemplate().indexOps(Organization.class).ensureIndex(
                new Index().on("roles", Direction.ASC));
        getTemplate().indexOps(Organization.class).ensureIndex(new Index().on("name", Direction.ASC));
        getTemplate().indexOps(DefaultLocation.class).ensureIndex(new Index().on("description", Direction.ASC));
        getLogger().info("Added mandatory Mongo indexes");
    }

    public void createCorruptionFlagsIndexes() {
        getTemplate().indexOps(Release.class).ensureIndex(new Index().on(FLAGS_TOTAL_FLAGGED, Direction.ASC));

        getTemplate().indexOps(Release.class).ensureIndex(new Index().on("flags.flaggedStats.type", Direction.ASC)
                .on("flags.flaggedStats.count", Direction.ASC)
        );

        getTemplate().indexOps(Release.class).ensureIndex(new Index().on("flags.eligibleStats.type", Direction.ASC)
                .on("flags.eligibleStats.count", Direction.ASC));
        getTemplate().indexOps(Release.class).ensureIndex(new Index().on(FlagsConstants.I038_VALUE, Direction.ASC));
        getTemplate().indexOps(Release.class).ensureIndex(new Index().on(FlagsConstants.I007_VALUE, Direction.ASC));
        getTemplate().indexOps(Release.class).ensureIndex(new Index().on(FlagsConstants.I004_VALUE, Direction.ASC));
        getTemplate().indexOps(Release.class).ensureIndex(new Index().on(FlagsConstants.I077_VALUE, Direction.ASC));
        getTemplate().indexOps(Release.class).ensureIndex(new Index().on(FlagsConstants.I180_VALUE, Direction.ASC));
        getTemplate().indexOps(Release.class).ensureIndex(new Index().on(FlagsConstants.I019_VALUE, Direction.ASC));
        getTemplate().indexOps(Release.class).ensureIndex(new Index().on(FlagsConstants.I002_VALUE, Direction.ASC));
        getTemplate().indexOps(Release.class).ensureIndex(new Index().on(FlagsConstants.I085_VALUE, Direction.ASC));
        getTemplate().indexOps(Release.class).ensureIndex(new Index().on(FlagsConstants.I171_VALUE, Direction.ASC));
        getLogger().info("Added corruption flags indexes");
    }

    @PostConstruct
    public void mongoPostInit() {
        createMandatoryImportIndexes();
        createPostImportStructures();
    }

    public void createPostImportStructures() {

        createCorruptionFlagsIndexes();


        // initialize some extra indexes
        getTemplate().indexOps(Release.class).ensureIndex(new Index().on("ocid", Direction.ASC).unique());

        getTemplate().indexOps(Release.class)
                .ensureIndex(new Index().on(MongoConstants.FieldNames.TENDER_PROC_METHOD, Direction.ASC));
        getTemplate().indexOps(Release.class)
                .ensureIndex(new Index().on("tender.procurementMethodRationale", Direction.ASC));
        getTemplate().indexOps(Release.class)
                .ensureIndex(new Index().on(MongoConstants.FieldNames.TENDER_STATUS, Direction.ASC));
        getTemplate().indexOps(Release.class)
                .ensureIndex(new Index().on(MongoConstants.FieldNames.AWARDS_STATUS, Direction.ASC));
        getTemplate().indexOps(Release.class)
                .ensureIndex(new Index().on(MongoConstants.FieldNames.AWARDS_SUPPLIERS_ID, Direction.ASC));
        getTemplate().indexOps(Release.class)
                .ensureIndex(new Index().on(MongoConstants.FieldNames.AWARDS_SUPPLIERS_NAME, Direction.ASC));
        getTemplate().indexOps(Release.class)
                .ensureIndex(new Index().on(MongoConstants.FieldNames.AWARDS_DATE, Direction.ASC));
        getTemplate().indexOps(Release.class)
                .ensureIndex(new Index().on(MongoConstants.FieldNames.AWARDS_VALUE_AMOUNT, Direction.ASC));
        getTemplate().indexOps(Release.class)
                .ensureIndex(new Index().on(MongoConstants.FieldNames.TENDER_VALUE_AMOUNT, Direction.ASC));
        getTemplate().indexOps(Release.class)
                .ensureIndex(new Index().on(MongoConstants.FieldNames.TENDER_NO_TENDERERS, Direction.ASC));
        getTemplate().indexOps(Release.class).ensureIndex(new Index().on(
                MongoConstants.FieldNames.TENDER_SUBMISSION_METHOD, Direction.ASC));
        getTemplate().indexOps(Release.class)
                .ensureIndex(new Index().on(MongoConstants.FieldNames.TENDER_PERIOD_START_DATE, Direction.ASC));
        getTemplate().indexOps(Release.class).ensureIndex(new Index()
                .on(MongoConstants.FieldNames.TENDER_PERIOD_END_DATE, Direction.ASC));
        getTemplate().indexOps(Release.class)
                .ensureIndex(new Index().on("tender.items.classification._id", Direction.ASC));
        getTemplate().indexOps(Release.class).ensureIndex(new Index().
                on("tender.items.deliveryLocation._id", Direction.ASC));
        getTemplate().indexOps(Release.class).ensureIndex(new Index().
                on("tender.items.deliveryLocation.geometry.coordinates", Direction.ASC));

        getTemplate().indexOps(Release.class).ensureIndex(new Index().
                on(MongoConstants.FieldNames.BIDS_DETAILS_TENDERERS_ID, Direction.ASC));
        getTemplate().indexOps(Release.class).ensureIndex(new Index().
                on(MongoConstants.FieldNames.BIDS_DETAILS_VALUE_AMOUNT, Direction.ASC));


        getTemplate().indexOps(Organization.class).ensureIndex(new TextIndexDefinitionBuilder()
                .withDefaultLanguage(MongoConstants.MONGO_LANGUAGE)
                .onField("name")
                .onField("id").onField("additionalIdentifiers._id").build());

        getTemplate().indexOps(Release.class).ensureIndex(new TextIndexDefinitionBuilder()
                .named("text_search")
                .withDefaultLanguage(MongoConstants.MONGO_LANGUAGE)
                .onFields("tender.title", "tender.description",
                        "tender.procuringEntity.name", "tender.id", "tender.procuringEntity.description",
                        "awards.id", "awards.description", "awards.suppliers.name", "awards.suppliers.description",
                        "ocid", "buyer.name", "buyer.id"
                ).build());

        getLogger().info("Added extra Mongo indexes");

        ScriptOperations scriptOps = getTemplate().scriptOps();

        // add script to calculate the percentiles endpoint
        URL scriptFile = getClass().getResource("/tenderBidPeriodPercentilesMongo.js");
        try {
            String scriptText = IOUtils.toString(scriptFile);
            ExecutableMongoScript script = new ExecutableMongoScript(scriptText);
            scriptOps.register(new NamedMongoScript("tenderBidPeriodPercentiles", script));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // add general mongo system helper methods
        URL systemScriptFile = getClass().getResource("/mongoSystemScripts.js");
        try {
            String systemScriptFileText = IOUtils.toString(systemScriptFile);
            ExecutableMongoScript script = new ExecutableMongoScript(systemScriptFileText);
            scriptOps.execute(script);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
