package com.sofa.pilot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sofa.pilot.dto.TeacherInput;
import com.sofa.pilot.dto.TeacherOutput;
import com.sofa.pilot.service.CrudService;

@RestController
@RequestMapping(path = "/api/teachers123456")
public class TeacherController extends BaseCrudController<TeacherInput, TeacherOutput> {

	@Autowired
	public TeacherController(CrudService<TeacherInput, TeacherOutput> crudService) {
		super(crudService);
	}
}
