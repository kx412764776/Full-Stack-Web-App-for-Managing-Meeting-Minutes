package com.apprenticeship.repository;

import com.apprenticeship.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("notificationJPARepository")
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
}
