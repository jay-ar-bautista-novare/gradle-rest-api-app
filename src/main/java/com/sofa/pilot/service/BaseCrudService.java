package com.sofa.pilot.service;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.VisibleForTesting;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.sofa.pilot.domain.BaseDomain;
import com.sofa.pilot.dto.ApiInput;
import com.sofa.pilot.dto.ApiOutput;
import com.sofa.pilot.exception.CrudServiceException;
import com.sofa.pilot.exception.EntityNotFoundException;

public abstract class BaseCrudService<D extends BaseDomain<O>, I extends ApiInput, O extends ApiOutput>
		implements CrudService<I, O> {

	private static final String NOT_FOUND = " not found";

	private final MongoRepository<D, String> mongoRepository;
	private final Class<D> domainClass;

	protected BaseCrudService(MongoRepository<D, String> mongoRepository, Class<D> domainClass) {
		super();
		this.mongoRepository = mongoRepository;
		this.domainClass = domainClass;
	}

	@Override
	public O create(I input) {
		D domain = createDomain(input);
		D result = mongoRepository.insert(domain);
		return result.generateOutput();
	}

	@Override
	public O read(String id) {
		Optional<D> optional = mongoRepository.findById(id);
		if (optional.isPresent()) {
			D result = optional.get();
			return result.generateOutput();
		}
		throw new EntityNotFoundException(domainClass.getSimpleName() + NOT_FOUND);
	}

	@Override
	public List<O> read() {
		List<D> results = mongoRepository.findAll();
		List<O> outputs = new ArrayList<>();
		results.forEach(result -> {
			O output = result.generateOutput();
			outputs.add(output);
		});
		return outputs;
	}

	@Override
	public O update(I input, String id) {
		boolean exists = mongoRepository.existsById(id);
		if (exists) {
			D domain = createDomain(input);
			domain.setId(id);
			D result = mongoRepository.save(domain);
			return result.generateOutput();
		}
		throw new EntityNotFoundException(domainClass.getSimpleName() + NOT_FOUND);
	}
	
		public O update2(I input, String id) {
		boolean exists = mongoRepository.existsById(id);
		if (exists) {
			D domain = createDomain(input);
			domain.setId(id);
			D result = mongoRepository.save(domain);
			return result.generateOutput();
		}
		throw new EntityNotFoundException(domainClass.getSimpleName() + NOT_FOUND);
	}
	
		public O update1(I input, String id) {
		boolean exists = mongoRepository.existsById(id);
		if (exists) {
			D domain = createDomain(input);
			domain.setId(id);
			D result = mongoRepository.save(domain);
			return result.generateOutput();
		}
		throw new EntityNotFoundException(domainClass.getSimpleName() + NOT_FOUND);
	}

	@Override
	public void delete(String id) {
		mongoRepository.deleteById(id);
	}

	public MongoRepository<D, String> getMongoRepository() {
		return mongoRepository;
	}

	@VisibleForTesting
	D createDomain(I input) {
		try {
			Constructor<D> constructor = domainClass.getConstructor(input.getClass());
			return constructor.newInstance(input);
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			throw new CrudServiceException("Error encountered while creating domain instance", e);
		}
	}
}
