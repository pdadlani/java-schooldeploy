package com.lambdaschool.school.service;

import com.lambdaschool.school.model.Useremail;

import java.util.List;

public interface UseremailService
{
    List<Useremail> findAll();

    Useremail findUseremailById(long id);

    List<Useremail> findByUserName(String username);

    void delete(long id, boolean isAdmin);

    Useremail save(Useremail useremail, boolean isAdmin);
}
