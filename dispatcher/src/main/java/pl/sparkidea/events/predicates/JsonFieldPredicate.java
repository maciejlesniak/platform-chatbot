package pl.sparkidea.events.predicates;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author Maciej Lesniak
 */
public final class JsonFieldPredicate implements Predicate<JsonNode> {

    private final String[] fieldNamePath;
    private final Predicate<JsonNode> valuePredicate;

    public final static Predicate<JsonNode> NULL = JsonNode::isNull;
    public final static Predicate<JsonNode> ANY_VALUE = NULL.negate();
    public final static Predicate<JsonNode> MISSING_FIELD = Objects::isNull;
    public final static Predicate<JsonNode> EMPTY_STRING = s -> s.asText().isEmpty();
    public final static Predicate<JsonNode> ZERO = z -> z.isDouble() && z.asDouble() == 0D;

    public JsonFieldPredicate(String path, Predicate<JsonNode> valuePredicate) {
        this.fieldNamePath = path.split("\\.");
        this.valuePredicate = valuePredicate;
    }

    public JsonFieldPredicate(String path) {
        this(path, ANY_VALUE);
    }

    @Override
    public boolean test(JsonNode jsonNode) {
        return Optional.ofNullable(jsonNode)
                .map(json -> {
                    JsonNode nodeByPath = getNodeByPath(json, fieldNamePath);

                    return valuePredicate.equals(MISSING_FIELD) && nodeByPath.isMissingNode()
                            || !nodeByPath.isMissingNode() && valuePredicate.test(nodeByPath);
                })
                .orElse(false);
    }

    private static JsonNode getNodeByPath(JsonNode rootNode, String[] path) {

        int i = 0;
        JsonNode currentNode = rootNode;
        while (i < path.length && !currentNode.isMissingNode()) {
            currentNode = currentNode.path(path[i++]);
        }

        return currentNode;
    }

}
