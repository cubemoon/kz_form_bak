package com.kuaizhan.form.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KZSite {

	private long siteId;
	private int status;
	private String domain;

}
