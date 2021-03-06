/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springsource.restbucks.payment;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springsource.restbucks.AbstractIntegrationTest;
import org.springsource.restbucks.order.Order;
import org.springsource.restbucks.order.TestUtils;
import org.springsource.restbucks.payment.PaymentAspectIntegrationTests.AdditionalConfig;

/**
 * @author Oliver Gierke
 */
@ContextConfiguration(classes = AdditionalConfig.class)
public class PaymentAspectIntegrationTests extends AbstractIntegrationTest {

	@Autowired
	PaymentService paymentService;
	@Autowired
	TestApplicationListener listener;

	@Configuration
	static class AdditionalConfig {

		@Bean
		public TestApplicationListener anotherEventListener() {
			return new TestApplicationListener();
		}
	}

	@Test
	public void publishesOrderPayedEvent() {

		Order order = TestUtils.createExistingOrder();
		paymentService.pay(order, new CreditCardNumber("1234123412341234"));

		assertThat(listener.invoked, is(true));
	}

	static class TestApplicationListener implements ApplicationListener<OrderPayedEvent> {

		boolean invoked = false;

		/* 
		 * (non-Javadoc)
		 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
		 */
		@Override
		public void onApplicationEvent(OrderPayedEvent event) {
			this.invoked = true;
		}
	}
}
