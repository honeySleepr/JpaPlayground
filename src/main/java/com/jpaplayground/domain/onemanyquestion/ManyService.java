package com.jpaplayground.domain.onemanyquestion;

import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManyService {

	private final OneRepository oneRepository;
	private final ManyRepository manyRepository;

	@Transactional
	public int save(Long oneId) {
		One one = oneRepository.findById(oneId).orElseThrow();

		Many many = new Many(one);
		one.addMany(many);
		manyRepository.save(many);
		System.out.println("@@@@@@@@ size : " + one.getManyList().size());
		return one.getManyList().size();
	}
}
