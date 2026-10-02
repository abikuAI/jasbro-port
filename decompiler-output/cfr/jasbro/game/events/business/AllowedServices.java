/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.events.business;

import jasbro.game.character.attributes.Sextype;
import java.util.HashMap;
import java.util.Map;

public class AllowedServices {
    private Map<Sextype, Boolean> allowedServicesMap;
    private boolean serviceMales = true;
    private boolean serviceFemales = true;
    private boolean serviceFutas = true;

    public AllowedServices() {
        this.allowedServicesMap = new HashMap<Sextype, Boolean>();
    }

    public AllowedServices(AllowedServices allowedServices) {
        this.serviceFemales = allowedServices.serviceFemales;
        this.serviceFutas = allowedServices.serviceFutas;
        this.serviceMales = allowedServices.serviceMales;
        this.allowedServicesMap = new HashMap<Sextype, Boolean>(allowedServices.allowedServicesMap);
    }

    public boolean isAllowed(Sextype sextype) {
        if (!this.allowedServicesMap.containsKey(sextype)) {
            this.allowedServicesMap.put(sextype, true);
        }
        return this.allowedServicesMap.get(sextype);
    }

    public void setAllowed(Sextype sextype, boolean allowed) {
        this.allowedServicesMap.put(sextype, allowed);
    }

    public boolean isServiceMales() {
        return this.serviceMales;
    }

    public void setServiceMales(boolean serviceMales) {
        this.serviceMales = serviceMales;
    }

    public boolean isServiceFemales() {
        return this.serviceFemales;
    }

    public void setServiceFemales(boolean serviceFemales) {
        this.serviceFemales = serviceFemales;
    }

    public boolean isServiceFutas() {
        return this.serviceFutas;
    }

    public void setServiceFutas(boolean serviceFutas) {
        this.serviceFutas = serviceFutas;
    }
}

