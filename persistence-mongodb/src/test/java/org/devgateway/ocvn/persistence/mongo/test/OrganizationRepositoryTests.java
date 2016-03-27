/**
 * 
 */
package org.devgateway.ocvn.persistence.mongo.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.devgateway.ocvn.persistence.mongo.ocds.Address;
import org.devgateway.ocvn.persistence.mongo.ocds.ContactPoint;
import org.devgateway.ocvn.persistence.mongo.ocds.Identifier;
import org.devgateway.toolkit.persistence.mongo.dao.VNOrganization;
import org.devgateway.toolkit.persistence.mongo.repository.VNOrganizationRepository;
import org.devgateway.toolkit.persistence.mongo.spring.MongoPersistenceApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;



/**
 * @author mihai
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(MongoPersistenceApplication.class)
public class OrganizationRepositoryTests {
	
	private static final Logger logger = LoggerFactory
			.getLogger(OrganizationRepositoryTests.class);

	@Autowired
	VNOrganizationRepository vnOrganizationRepository;

	
	@Before
	public void init() {
		vnOrganizationRepository.deleteAll();
	}
	
	@Test
	public void saveOrganization() {

		VNOrganization o=new VNOrganization();
		Address a=new Address();
		a.setCountryName("United States");
		a.setLocality("Washington");
		a.setRegion("DC");
		a.setPostalCode("20005");
		a.setStreetAddress("1110 Vermont Ave. NW, Suite 500");
		o.setAddress(a);
		
		ContactPoint cp=new ContactPoint();
		cp.setEmail("info@developmentgateway.org");
		cp.setName("John Doe");
		cp.setTelephone("555-1234567");
		cp.setUrl("http://developmentgateway.org");
		cp.setFaxNumber("555-7654321");
		o.setContactPoint(cp);;
		
		Identifier i=new Identifier();
		i.setId("DG");
		i.setLegalName("Development Gateway");
		o.setIdentifier(i);
		
		o.setProcuringEntity(true);
		
		o.getAdditionalIdentifiers().add(i);
		
		
		VNOrganization save = vnOrganizationRepository.save(o);
		
		assertThat(save.getId(), is(not(nullValue())));
		
		logger.info(save.getId());
		
	}

}