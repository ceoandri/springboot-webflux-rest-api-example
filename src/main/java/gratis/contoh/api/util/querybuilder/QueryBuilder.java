package gratis.contoh.api.util.querybuilder;

import java.util.ArrayList;

import gratis.contoh.api.util.pagination.PaginationRequest;

public class QueryBuilder {
	
	public static String builder(String baseQuery, 
			ArrayList<Criteria> criterias) {
		String generatedCriteria = generateCriteria(criterias);
		
		return join(baseQuery, generatedCriteria, "");
	}
	
	public static String countBuilder(String baseQuery, 
			ArrayList<Criteria> criterias) {
		String generatedCriteria = generateCriteria(criterias);
		
		return joinCount(baseQuery, generatedCriteria);
	}
	
	public static String builder(String baseQuery, 
			PaginationRequest paginationRequest) {
		String generatedPagination = generatePagination(paginationRequest);

		return join(baseQuery, "", generatedPagination);
	}
	
	public static String builder(String baseQuery, 
			ArrayList<Criteria> criterias, 
			PaginationRequest paginationRequest) {
		String generatedCriteria = generateCriteria(criterias);
		String generatedPagination = generatePagination(paginationRequest);
		
		return join(baseQuery, generatedCriteria, generatedPagination);
	}
	
	private static String generateCriteria(ArrayList<Criteria> criterias) {
		String result = "";
		
		for (int i = 0; i < criterias.size(); i++) {
			result = String.format("%s %s", result, operatorHandler(criterias.get(i)));
		}
		
		result = result.trim();
		
		if (result.startsWith("AND")) {
			result = result.substring(3);
		} else {
			result = result.substring(2);
		}
		
		return result;
	}
	
	private static String operatorHandler(Criteria criteria) {
		if (criteria.getCriteria() != null && !criteria.getCriteria().isEmpty()) {
			switch (criteria.getOperator()) {
			case AND: {
				return String.format("AND ( %s )", generateCriteria(criteria.getCriteria()));
			}
			case OR: {
				return String.format("OR (  %s )", generateCriteria(criteria.getCriteria()));
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + criteria.getOperator());
			} 
		} else {
			switch (criteria.getOperator()) {
			case AND: {
				return String.format("AND %s %s", criteria.getColumn(), conditionHandler(criteria));
			}
			case OR: {
				return String.format("OR %s %s", criteria.getColumn(), conditionHandler(criteria));
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + criteria.getOperator());
			}
		}
	}
	
	private static String conditionHandler(Criteria criteria) {
		switch (criteria.getCondition()) {
		case EQUAL: {
			return String.format("= %s", valueTypeHandler(criteria.getValueType(),
					criteria.getValue()[0]));
		}
		case NOT_EQUAL: {
			return String.format("!= %s", valueTypeHandler(criteria.getValueType(),
					criteria.getValue()[0]));
		}
		case LIKE: {
			return "LIKE ('%" + criteria.getValue()[0] + "%')";
		}
		case START_WITH: {
			return "LIKE ('" + criteria.getValue()[0] + "%')";
		}
		case END_WITH: {
			return "LIKE ('%" + criteria.getValue()[0] + "')";
		}
		case GREATER_THAN: {
			return String.format("> %s", valueTypeHandler(criteria.getValueType(),
					criteria.getValue()[0]));
		}
		case GREATER_THAN_EQUALS: {
			return String.format(">= %s", valueTypeHandler(criteria.getValueType(),
					criteria.getValue()[0]));
		}
		case LESS_THAN: {
			return String.format("< %s", valueTypeHandler(criteria.getValueType(),
					criteria.getValue()[0]));
		}
		case LESS_THAN_EQUALS: {
			return String.format("<= %s", valueTypeHandler(criteria.getValueType(),
					criteria.getValue()[0]));
		}
		case BETWEEN: {
			return String.format("BETWEEN %s AND %s", valueTypeHandler(criteria.getValueType(),
					criteria.getValue()[0]), valueTypeHandler(criteria.getValueType(),
							criteria.getValue()[0]));
		}
		case IS_NULL: {
			return "IS NULL";
		}
		case IS_NOT_NULL: {
			return "IS NOT NULL";
		}
		case IN: {
			return String.format("IN (%s)", arrayConditionHandler(criteria.getValueType(), criteria.getValue()));
		}
		case NOT_IN: {
			return String.format("NOT IN (%s)", arrayConditionHandler(criteria.getValueType(), criteria.getValue()));
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + criteria.getCondition());
		}
	}
	
	private static String valueTypeHandler(ValueType valueType, String value) {
		switch (valueType) {
		case TEXT: {
			return String.format("'%s'", value);
		}
		case NUMBER: {
			return value;
		}
		case DATE: {
			return String.format("'%s'", value);
		}
		case TIMESTAMP: {
			return value;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + valueType);
		}
	}

	private static String arrayConditionHandler(ValueType valueType, String[] value) {
		String result = "";

		for (int i = 0; i < value.length; i++) {
			result += "," + valueTypeHandler(valueType, value[i]);
		}

		return result.substring(1);
	}
	
	private static String generatePagination(PaginationRequest paginationRequest) {
		int pageNumber = paginationRequest.getPage();
		int limit = paginationRequest.getSize();
		int offset = (limit * pageNumber) - limit;
		String order = paginationRequest.getSort();
		
		return "ORDER BY " + order + " LIMIT " + limit + " OFFSET " + offset;
	}
	
	private static String join(String base, String criteria, String pagination) {
		String res = String.format("SELECT res.* FROM (%s) res", base);
		if (!criteria.isEmpty() && !criteria.isBlank()) {
			res = String.format("%s WHERE %s", res, criteria);
		}
		if (!pagination.isEmpty() && !pagination.isBlank()) {
			res = String.format("%s %s", res, pagination);
		}
		return res;
	}
	
	private static String joinCount(String base, String criteria) {
		String res = String.format("SELECT count(res.*) FROM (%s) res", base);
		if (!criteria.isEmpty() && !criteria.isBlank()) {
			res = String.format("%s WHERE %s", res, criteria);
		}
		return res;
	}
}
