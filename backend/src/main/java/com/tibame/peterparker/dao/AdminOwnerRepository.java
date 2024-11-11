package com.tibame.peterparker.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tibame.peterparker.entity.Owner;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminOwnerRepository extends JpaRepository<Owner, Integer> {
    Owner findByOwnerAccount(String ownerAccount);
}
