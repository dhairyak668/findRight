package com.pluralsight.conferencedemo.repositories;

import com.pluralsight.conferencedemo.models.Speakers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpeakerRepository extends JpaRepository<Speakers, Long> {
}
