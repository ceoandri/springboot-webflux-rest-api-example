package gratis.contoh.api.util.querybuilder;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Data;

@Data
public class Criteria implements Serializable {

	private static final long serialVersionUID = 9174524865007874643L;
	
	private Operator operator;
	private Condition condition;
	private String column;
	private String[] value;
	private ArrayList<Criteria> criteria;
	private ValueType valueType;
	
	public Criteria(Operator operator, Condition condition, String column, String[] value, ValueType valueType) {
		this.operator = operator;
		this.condition = condition;
		this.column = column;
		this.value = value;
		this.valueType = valueType;
	}
	
	public Criteria(Operator operator, ArrayList<Criteria> criteria) {
		this.operator = operator;
		this.criteria = criteria;
	}
	
}
