#import <React/RCTBridgeModule.h>
#import "MfsIOSLibrary.h"

@interface RnMasterpass : NSObject <RCTBridgeModule>
{
    // masterpass kütüphanesini init ettikten sonra, kullanıcı bilgilerini class içerisine set ediyoruz.
    MfsIOSLibrary *mfsLib;
    NSString *clientId;
    NSString *macroMerchantId;
    NSString *phone;
    NSString *lang;
    NSString *env;
    RCTResponseSenderBlock anyCallback;
}

-(NSString *)checkIfEmpty:(NSObject*)target key:(NSString*)key; // NSObject içerisinde ki key'in karşılığı olan değerin null/nill olup olmadığını kontrol eder.

// Aşağıda ki metodlar masterpass native callbackleridir, içerikleri masterpass entegrasyon yönergelerinde bulunmaktadır.
-(void) checkMasterPassCallback: (MfsResponse*)mfsResponse;
-(void) getCardForRecurringPaymentCallback:(MfsResponse*)mfsResponse;
-(void)purchaseCallBack:(MfsResponse*)actionResult;
-(void)deleteCardCallback: (MfsResponse*)actionResult;

@end
