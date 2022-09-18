package com.jpaplayground.conversation;

public enum UserType {
	Seller,
	Buyer,
	All;

	public UserType counterPart() {
		if (this.equals(Seller)) {
			return Buyer;
		} else {
			return Seller;
		}
	}
}
