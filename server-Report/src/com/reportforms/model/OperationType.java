package com.reportforms.model;

public class OperationType extends BaseDomain{
    /**
	 * 
	 */
	private static final long serialVersionUID = -719528173351117839L;

	private Integer id;

    private String name;

    private Integer seq;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }
}