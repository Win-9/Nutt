package com.backend.nutt.repository;

import com.backend.nutt.domain.AccessToken;
import com.backend.nutt.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface AccessTokenRepository extends CrudRepository<AccessToken, String> {

}
