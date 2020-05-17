package it.polito.tdp.extflightdelays.model;

public class Rotta {

	Airport ap;
	Airport ad;
	int peso;
	/**
	 * @param ap
	 * @param ad
	 * @param peso
	 */
	public Rotta(Airport ap, Airport ad, int peso) {
		super();
		this.ap = ap;
		this.ad = ad;
		this.peso = peso;
	}
	public Airport getAp() {
		return ap;
	}
	public void setAp(Airport ap) {
		this.ap = ap;
	}
	public Airport getAd() {
		return ad;
	}
	public void setAd(Airport ad) {
		this.ad = ad;
	}
	public int getPeso() {
		return peso;
	}
	public void setPeso(int peso) {
		this.peso = peso;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ad == null) ? 0 : ad.hashCode());
		result = prime * result + ((ap == null) ? 0 : ap.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rotta other = (Rotta) obj;
		if (ad == null) {
			if (other.ad != null)
				return false;
		} else if (!ad.equals(other.ad))
			return false;
		if (ap == null) {
			if (other.ap != null)
				return false;
		} else if (!ap.equals(other.ap))
			return false;
		return true;
	}
	
	
	
}
