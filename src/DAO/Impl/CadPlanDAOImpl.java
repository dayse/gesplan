 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package DAO.Impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.SQLQuery;

import util.JPAUtil;

import modelo.CadPlan;
import modelo.PerioPM;
import modelo.PlPerMod;
import DAO.CadPlanDAO;
import DAO.PerioPMDAO;
import DAO.PlPerModDAO;
import DAO.PlanoModeloDAO;
import DAO.controle.FabricaDeDao;
import DAO.exception.ObjetoNaoEncontradoException;
import DAO.generico.JPADaoGenerico;

/**
 * As classes DAOImpl implementam aqueles métodos que são específicos,
 * ou que ainda não foram generalizados
 * 
 * @author daysemou
 *
 */
public abstract class CadPlanDAOImpl
       extends JPADaoGenerico<CadPlan, Long> implements CadPlanDAO 
{   
    public CadPlanDAOImpl()
    { 	super(CadPlan.class); 
    }
    
	@SuppressWarnings("unchecked")
	public final List recuperaCadPlanComDependencias(CadPlan cadPlan){
	
		PlPerModDAO plPerModDAO = null;
		PlanoModeloDAO planoModeloDAO = null;
		PerioPMDAO perioPMDAO = null;
		
		PlPerMod plPerMod = null;
		
		try {
			plPerModDAO = FabricaDeDao.getDao(PlPerModDAOImpl.class);
			planoModeloDAO = FabricaDeDao.getDao(PlanoModeloDAOImpl.class);
			perioPMDAO = FabricaDeDao.getDao(PerioPMDAOImpl.class);
		} catch (Exception e) {
		}
		
		try {
			
			EntityManager em = JPAUtil.getEntityManager();
			
			String sql = "select distinct(c.*), p.*, pl.* " +
						 "from cadplan c " +
						 "inner join planomodelo p on p.cadplan_id = c.id " +
						 "inner join plpermod pl on pl.planomodelo_id = p.id " +
						 "where c.codplan = ? ";
			
			final Query querySQL = em.createNativeQuery(sql);
			querySQL.setParameter(1, cadPlan.getCodPlan());
			ResultSet rs = (ResultSet) querySQL.getResultList();
			
			while (rs.next()){
				
				PerioPM periodo = perioPMDAO.getPorId(rs.getLong("periopm_id"));

			}
			
		} catch (SQLException ex){
			
		} catch (ObjetoNaoEncontradoException e) {
			
		}
		
		
		return null;
	}
}
    
