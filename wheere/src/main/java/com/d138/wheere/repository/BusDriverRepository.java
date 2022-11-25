package com.d138.wheere.repository;

import com.d138.wheere.domain.BusDriver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BusDriverRepository {

    private final EntityManager em;

    public Long save(BusDriver busDriver) {
        em.persist(busDriver);
        return busDriver.getId();
    }

    /**
     * 버스 ID와 운행날짜로 BusDriver 조회
     * 버스 운행이 중복되는지 검사하기 위해 사용됨
     * 버스기사 버스 배정 시 호출됨
     *
     * @param busId
     * @param operationDate
     * @return
     */
    public List<BusDriver> findBusDriverByBusAndDate(Long busId, LocalDate operationDate) {
        return em.createQuery("select bd from BusDriver bd"
                        + " join bd.bus b on b.id = :busId"
                        + " where bd.operationDate = :operationDate", BusDriver.class)
                .setParameter("busId", busId)
                .setParameter("operationDate", operationDate)
                .getResultList();
    }

    /**
     * 버스 ID와 운행날짜로 BusDriver와 Bus 조회
     * 버스기사 평점 반영 시 호출됨
     * Bus를 페치 조인으로 함께 불러옴
     *
     * @param busId
     * @param operationDate
     * @return
     */
    public BusDriver findBusDriverWithDriver(Long busId, LocalDate operationDate) {
        return em.createQuery("select bd from BusDriver bd"
                        + " join bd.bus b on b.id = :busId"
                        + " join fetch bd.driver"
                        + " where bd.operationDate = :operationDate", BusDriver.class)
                .setParameter("busId", busId)
                .setParameter("operationDate", operationDate)
                .getSingleResult();
    }

    public List<BusDriver> findBusDriverListWithDriver(Long busId, LocalDate operationDate) {
        return em.createQuery("select bd from BusDriver bd"
                        + " join bd.bus b on b.id = :busId"
                        + " where bd.operationDate = :operationDate", BusDriver.class)
                .setParameter("busId", busId)
                .setParameter("operationDate", operationDate)
                .getResultList();
    }
}
