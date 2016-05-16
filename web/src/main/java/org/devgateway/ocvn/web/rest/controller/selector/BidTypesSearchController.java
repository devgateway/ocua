/*******************************************************************************
 * Copyright (c) 2015 Development Gateway, Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 *
 * Contributors:
 * Development Gateway - initial API and implementation
 *******************************************************************************/
package org.devgateway.ocvn.web.rest.controller.selector;

import java.util.List;

import org.devgateway.ocds.persistence.mongo.Classification;
import org.devgateway.ocds.persistence.mongo.repository.ClassificationRepository;
import org.devgateway.ocvn.web.rest.controller.GenericOcvnController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author mpostelnicu
 * 
 */
@RestController
public class BidTypesSearchController extends GenericOcvnController {

	@Autowired
	private ClassificationRepository classificationRepository;

	@RequestMapping(value = "/api/ocds/bidType/all", method = RequestMethod.GET)
	public List<Classification> bidTypes() {

		return classificationRepository.findAll(new Sort(Direction.ASC, "description"));

	}

}