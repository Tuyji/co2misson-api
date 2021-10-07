package com.co2nsensus.co2mission.model.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Type;

@MappedSuperclass
public class EntityWithUUID implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1391072478993374251L;
	
	@Id
	@Type(type = "pg-uuid")
	protected UUID id;

	public EntityWithUUID() {
		if(id==null)
			this.id = UUID.randomUUID();
	}

	public UUID getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		EntityWithUUID other = (EntityWithUUID) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	

}