import { NgModule } from '@angular/core';

import { BankAccountKataSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [BankAccountKataSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [BankAccountKataSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class BankAccountKataSharedCommonModule {}
