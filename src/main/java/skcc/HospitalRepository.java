package skcc;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface HospitalRepository extends PagingAndSortingRepository<Hospital, Long>{

    List<Hospital> findByHospitalId(String HospitalId);

}