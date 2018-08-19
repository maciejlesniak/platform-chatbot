package pl.sparkidea.events.predicates

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Specification

import java.util.function.Predicate

/**
 *
 * @author Maciej Lesniak
 */
class JsonFieldPredicateTest extends Specification {

    def "test should validate Json by path"() {

        expect:
        new JsonFieldPredicate(path, predicate as Predicate<JsonNode>).test(new ObjectMapper().readTree(json))

        where:
        json                             | path                               | predicate
        """{"pl": {"spark": "media"}}""" | "pl.spark"                         | { val -> val.asText() == "media" }
        """{"pl": {"spark": "media"}}""" | "pl.spark"                         | JsonFieldPredicate.ANY_VALUE
        """{"pl": {"spark": "media"}}""" | "pl.spark.some_non_existing_field" | JsonFieldPredicate.MISSING_FIELD
        """{"pl": {"spark": null}}"""    | "pl.spark"                         | JsonFieldPredicate.NULL

    }

    def "test should NOT validate Json by path"() {

        expect:
        !new JsonFieldPredicate(path, predicate as Predicate<JsonNode>).test(new ObjectMapper().readTree(json))

        where:
        json                             | path                               | predicate
        """{"pl": {"spark": "media"}}""" | "pl.spark"                         | { val -> val.asText() == "other_media" }
        """{"pl": {"spark": "media"}}""" | "pl.spark.some_non_existing_field" | JsonFieldPredicate.NULL
        """{"pl": {"spark": null}}"""    | "pl.spark"                         | JsonFieldPredicate.ANY_VALUE
        """{"pl": {"spark": null}}"""    | "pl.spark"                         | JsonFieldPredicate.MISSING_FIELD
        """{"pl": {"spark": "media"}}""" | "pl.spark"                         | JsonFieldPredicate.EMPTY_STRING
        """{"pl": {"spark": "media"}}""" | "pl.spark"                         | JsonFieldPredicate.ZERO
        """{"pl": {"spark": 5}}"""       | "pl.spark"                         | JsonFieldPredicate.ZERO
    }

    def "test should validate Json when any value is existing"() {

        expect:
        new JsonFieldPredicate(path).test(new ObjectMapper().readTree(json))

        where:
        json                             | path
        """{"pl": {"spark": "media"}}""" | "pl"
        """{"pl": {"spark": "media"}}""" | "pl.spark"

    }

}
