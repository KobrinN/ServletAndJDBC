package org.example.service;

import java.util.List;

public interface Service <Request, Response> {
    Response get(Long id);
    List<Response> getAll();
    Long post(Request request);
    void delete(Long id);
    Response edit(Long id, Request request);
}
