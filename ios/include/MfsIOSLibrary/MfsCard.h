//
//  Card.h
//  MfsIOSLibrary
//
//  Created by ercu on 17/02/15.
//  Copyright (c) 2015 Phaymobile. All rights reserved.
//

#ifndef MfsIOSLibrary_Card_h
#define MfsIOSLibrary_Card_h

@interface MfsCard : NSObject
@property (nonatomic, copy) NSString *cardName;
@property (nonatomic, copy) NSString *cardNo;
@property (nonatomic, copy) NSString *expireMonth;
@property (nonatomic, copy) NSString *cardStatus;
@property (nonatomic, copy) NSString *expireYear;
@property (nonatomic, copy) NSString *cardHolderName;
@property (nonatomic, copy) NSString *cvv;

@property (nonatomic) BOOL isMasterPass;
@property (strong, nonatomic)  NSString *bankIca;
@property (strong, nonatomic)  NSString *loyaltyCode;
@property (strong, nonatomic)  NSString *productName;
@property (strong, nonatomic)  NSString *cardId;
@property (strong, nonatomic)  NSString *eftCode;

@end

@protocol MfsCard
@end

#endif
