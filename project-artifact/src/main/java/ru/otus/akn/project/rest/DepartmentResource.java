package ru.otus.akn.project.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import ru.otus.akn.project.db.entity.DepartmentEntity;
import ru.otus.akn.project.rest.model.Department;
import ru.otus.akn.project.util.EntityManagerControlGeneric;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ru.otus.akn.project.db.dao.DepartmentsDAO.*;
import static ru.otus.akn.project.util.PersistenceUtil.MANAGER_FACTORY;

@Path("/departments")
@Api(tags = "Departments")
@SwaggerDefinition(tags = {
        @Tag(name = "Departments", description = "RESTful API to manage with departments")
})
public class DepartmentResource {

    @GET
    @ApiOperation("Get all departments")
    public Response read() {
        List<DepartmentEntity> allDepartments;

        try {
            allDepartments = new EntityManagerControlGeneric<List<DepartmentEntity>>(MANAGER_FACTORY) {
                @Override
                public List<DepartmentEntity> requestMethod(EntityManager manager) {
                    return getAllDepartmentEntities(manager);
                }
            }.processRequest();
        } catch (Exception e) {
            throw new WebApplicationException("Something went wrong when tried to get department entities from DB.", e);
        }

        List<Department> result = new ArrayList<>();
        for (DepartmentEntity entity : allDepartments) {
            result.add(convertDepartmentEntityToDepartmentClient(entity));
        }
        return Response.status(200).entity(result).build();
    }

    @GET
    @ApiOperation("Get department by id")
    @Path("/{departmentId}")
    public Response read(@PathParam("departmentId") Long departmentId) {
        DepartmentEntity departmentEntity;

        try {
            departmentEntity = new EntityManagerControlGeneric<DepartmentEntity>(MANAGER_FACTORY) {
                @Override
                public DepartmentEntity requestMethod(EntityManager manager) {
                    return getDepartmentEntity(manager, departmentId);
                }
            }.processRequest();
        } catch (Exception e) {
            throw new WebApplicationException("Something went wrong when tried to get department entity from DB by id.", e);
        }

        return Response.status(200).entity(convertDepartmentEntityToDepartmentClient(departmentEntity)).build();
    }

    @POST
    @ApiOperation("Create department")
    public Response create(@Valid @BeanParam Department department) {
        DepartmentEntity saved;
        try {
            saved = saveDepartment(convertDepartmentClientToDepartmentEntity(department));
        } catch (Exception e) {
            throw new WebApplicationException("Something went wrong when tried to create department entity.", e);
        }
        return Response.status(201).entity(saved.getDepartmentId()).build();
    }

    @PUT
    @ApiOperation("Update department")
    @Path("/{departmentId}")
    public Response update(@PathParam("departmentId") Long id, @Valid @BeanParam Department department) {
        department.setDepartmentId(id);
        try {
            updateAllDepartments(Collections.singletonList(convertDepartmentClientToDepartmentEntity(department)));
        } catch (Exception e) {
            throw new WebApplicationException("Something went wrong when tried to update department entity.", e);
        }
        return Response.ok().build();
    }

    @DELETE
    @ApiOperation("Delete department by id")
    @Path("/{departmentId}")
    public Response delete(@PathParam("departmentId") Long id) {
        try {
            deleteDepartmentEntityById(id);
        } catch (Exception e) {
            throw new WebApplicationException("Something went wrong when tried to delete department entity.", e);
        }
        return Response.status(204).build();
    }

    private Department convertDepartmentEntityToDepartmentClient(DepartmentEntity entity) {
        Department department = new Department();
        department.setDepartmentId(entity.getDepartmentId());
        department.setDepartmentName(entity.getDepartmentName());
        department.setCity(entity.getCity());
        department.setFullAddress(entity.getFullAddress());
        return department;
    }

    private DepartmentEntity convertDepartmentClientToDepartmentEntity(Department department) {
        DepartmentEntity entity = new DepartmentEntity();
        entity.setDepartmentId(department.getDepartmentId());
        entity.setDepartmentName(department.getDepartmentName());
        entity.setCity(department.getCity());
        entity.setFullAddress(department.getFullAddress());
        return entity;
    }

}
