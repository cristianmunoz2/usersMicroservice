package com.pragma.usersMicroservice.domain.spi;

import com.pragma.usersMicroservice.domain.model.Role;
import com.pragma.usersMicroservice.domain.util.RoleName;

public interface IRolePersistencePort {
    Role findByName(RoleName name);
}
