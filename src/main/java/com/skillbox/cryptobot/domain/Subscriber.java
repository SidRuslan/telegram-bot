package com.skillbox.cryptobot.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "subscribers")
@Data
@NoArgsConstructor
public class Subscriber {

    @Id()
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    @Column(name = "telegram_id")
    Long telegramId;

    @Column(name = "price")
    Double price;
}
