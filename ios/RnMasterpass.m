#import "RnMasterpass.h"

#define isEqualIgnoreCaseToString(string1, string2) ([string1 caseInsensitiveCompare:string2] == NSOrderedSame)

@implementation RnMasterpass

RCT_EXPORT_MODULE()

-(NSString *)checkIfEmpty:(NSObject*)target key:(NSString*)key
{
    NSString *value = [target valueForKey:key];
    return value ? value : @"";
}

RCT_EXPORT_METHOD(sampleMethod:(NSString *)stringArgument numberParameter:(nonnull NSNumber *)numberArgument callback:(RCTResponseSenderBlock)callback)
{
    // TODO: Implement some actually useful functionality
    callback(@[[NSString stringWithFormat: @"numberArgument: %@ stringArgument: %@", numberArgument, stringArgument]]);
}

RCT_EXPORT_METHOD(initialize: (NSDictionary*)params callback:(RCTResponseSenderBlock)callback)
{
    // Object variables
    clientId = [params valueForKey:@"clientId"];
    macroMerchantId = [params valueForKey:@"macroMerchantId"];
    phone = [params valueForKey:@"phone"];
    lang = [params valueForKey:@"language"];
    env = [params valueForKey:@"env"];
    
    mfsLib = [[MfsIOSLibrary alloc] initWithMsisdn: phone];
    [mfsLib setClientId: clientId];
    [mfsLib setMacroMerchantId: macroMerchantId];
    [mfsLib setLanguage: lang];
    
    if(isEqualIgnoreCaseToString(env, @"uatui")) {
        [mfsLib setBaseURL: @"https://uatui.masterpassturkiye.com/v2"];
    } else if(isEqualIgnoreCaseToString(env, @"test")) {
        [mfsLib setBaseURL: @"https://test.masterpassturkiye.com/MasterpassJsonServerHandler/v2"];
    } else if(isEqualIgnoreCaseToString(env, @"prod")) {
        [mfsLib setBaseURL: @"https://masterpassturkiye.com/v2"];
    }
    
    callback(@[[NSString stringWithFormat: @"[+] Masterpass Initialized"]]);
}

RCT_EXPORT_METHOD(checkMasterpass:(NSString *)token referenceNo:(NSString *)refNo callback:(RCTResponseSenderBlock)callback)
{
    anyCallback = callback;
    [mfsLib checkMasterPassEndUser:phone token:token checkCallback:@selector(checkMasterPassCallback:) checkTarget:self];
}

-(void) checkMasterPassCallback: (MfsResponse*)mfsResponse
{
    if(mfsResponse.result) {
        anyCallback(@[[NSString stringWithFormat: @"%@", mfsResponse.cardStatus]]);
    }
}

RCT_EXPORT_METHOD(getCards:(NSString *)token referenceNo:(NSString *)refNo callback:(RCTResponseSenderBlock)callback)
{
    anyCallback = callback;
    [mfsLib getCards:@selector(getCardForRecurringPaymentCallback:) token:token getCardsTarget:self];
}

-(void) getCardForRecurringPaymentCallback:(MfsResponse*)mfsResponse{
    
    if(mfsResponse.result){
        NSMutableArray* arr = [NSMutableArray array];
        for (MfsCard *card in mfsResponse.cardList)
        {
            NSDictionary * dict = @{
                @"Name": [self checkIfEmpty:card key:@"cardName"],
                @"Value1": [self checkIfEmpty:card key:@"cardNo"],
                @"UniqueId": [self checkIfEmpty:card key:@"cardId"]
            };

            [arr addObject:dict];
        }
        
        NSError *error;
        NSData *jsonData = [NSJSONSerialization dataWithJSONObject:arr options:NSJSONWritingPrettyPrinted error:&error];
        
        if (! jsonData) {
            NSLog(@"Got an error: %@", error);
        } else {
            NSString *jsonString = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
            NSLog(@"FUCK %@", jsonString);
            anyCallback(@[[NSString stringWithFormat: @"%@", jsonString]]);
        }
    } else {
        anyCallback(@[[NSString stringWithFormat: @"[]"]]);
    }
}

RCT_EXPORT_METHOD(purchase:(NSString *)token referenceNo:(NSString *)refno cardName:(NSString *)cardName orderNo:(NSString *)orderNo total:(nonnull NSNumber *)total callback:(RCTResponseSenderBlock)callback)
{
    anyCallback = callback;

    float numberToFloat = [total floatValue];
    numberToFloat = numberToFloat * 100;
    int floatToInt = ceil(numberToFloat);
    NSString *strTotal = [NSString stringWithFormat:@"%d", floatToInt];
    NSLog(@"%@", strTotal);
    [mfsLib pay:cardName token:token amount:strTotal cvv:nil orderId:orderNo payCallback:@selector(purchaseCallBack:) payTarget:self];
}

-(void)purchaseCallBack:(MfsResponse*)actionResult
{
    if(actionResult.result){
        NSMutableArray* arr = [NSMutableArray array];
        NSDictionary * dict = @{
            @"responseCode": [self checkIfEmpty:actionResult key:@"errorCode"],
            @"responseDesc": [self checkIfEmpty:actionResult key:@"errorDescription"],
            @"token": [self checkIfEmpty:actionResult key:@"token"]};

        [arr addObject:dict];
        NSError *error;
        NSData *jsonData = [NSJSONSerialization dataWithJSONObject:arr options:NSJSONWritingPrettyPrinted error:&error];

        if (! jsonData) {
            NSLog(@"Got an error: %@", error);
        } else {
            NSString *jsonString = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
            anyCallback(@[[NSString stringWithFormat: @"%@", jsonString]]);
        }
    } else {
        anyCallback(@[[NSString stringWithFormat: @"{}"]]);
    }
}

RCT_EXPORT_METHOD(deleteCard:(NSString *)itemName token:(NSString *)token referenceNo:(NSString *)referenceNo callback:(RCTResponseSenderBlock)callback)
{
    anyCallback = callback;
    [mfsLib deleteCard:itemName token:token deleteCallback:@selector(deleteCardCallback:) deleteTarget:self];
}

-(void)deleteCardCallback: (MfsResponse*)actionResult
{
    if(actionResult.result){
        NSMutableArray* arr = [NSMutableArray array];
        NSDictionary * dict = @{
            @"refNo": [self checkIfEmpty:actionResult key:@"refNo"],
            @"responseDesc": [self checkIfEmpty:actionResult key:@"errorDescription"]
        };

        [arr addObject:dict];
        NSError *error;
        NSData *jsonData = [NSJSONSerialization dataWithJSONObject:arr options:NSJSONWritingPrettyPrinted error:&error];

        if (! jsonData) {
            NSLog(@"Got an error: %@", error);
        } else {
            NSString *jsonString = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
            anyCallback(@[[NSString stringWithFormat: @"%@", jsonString]]);
        }
    } else {
        anyCallback(@[[NSString stringWithFormat: @"{}"]]);
    }
}

@end
