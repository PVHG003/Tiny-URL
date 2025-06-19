package vn.pvhg.tinyurl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.pvhg.tinyurl.model.VisitLog;

public interface VisitLogRepository extends JpaRepository<VisitLog, Long> {
}
