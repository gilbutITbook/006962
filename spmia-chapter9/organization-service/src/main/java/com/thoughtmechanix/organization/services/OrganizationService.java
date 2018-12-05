package com.thoughtmechanix.organization.services;

import com.thoughtmechanix.organization.events.source.SimpleSourceBean;
import com.thoughtmechanix.organization.model.Organization;
import com.thoughtmechanix.organization.repository.OrganizationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import brave.Tracer;
import brave.Tracer.SpanInScope;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class OrganizationService {
    @Autowired
    private OrganizationRepository orgRepository;

    @Autowired
    private Tracer tracer;

    @Autowired
    SimpleSourceBean simpleSourceBean;

    private static final Logger logger = LoggerFactory.getLogger(OrganizationService.class);

    public Organization getOrg(String organizationId) {

    	brave.Span newSpan = tracer.nextSpan().name("getOrgDBCall");
      logger.debug("In the organizationService.getOrg() call");
      try (SpanInScope ws = tracer.withSpanInScope(newSpan.start())){
          return orgRepository.findById(organizationId).get();
      }
      finally{
        newSpan.tag("peer.service", "postgres");
        newSpan.annotate("cr");
        newSpan.finish();
      }
    }

    public void saveOrg(Organization org){
        org.setId( UUID.randomUUID().toString());

        orgRepository.save(org);
        simpleSourceBean.publishOrgChange("SAVE", org.getId());
    }

    public void updateOrg(Organization org){
        orgRepository.save(org);
        simpleSourceBean.publishOrgChange("UPDATE", org.getId());

    }

    public void deleteOrg(String orgId){
        orgRepository.deleteById( orgId );
        simpleSourceBean.publishOrgChange("DELETE", orgId);
    }
}
