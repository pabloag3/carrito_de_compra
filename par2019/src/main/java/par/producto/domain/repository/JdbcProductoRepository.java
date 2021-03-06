package par.producto.domain.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import par.util.DBUtils;
import par.producto.domain.model.entity.Entity;
import par.producto.domain.model.entity.Producto;

/**
 *
 * @author Pablo Aguilar
 */
//@Repository("productoRepository")
public class JdbcProductoRepository implements ProductoRepository<Producto, Integer> {

    private Map<String, Producto> entities;

    /**
     * Check if given user name already exist.
     *
     * @param descripcion
     * @return true if already exist, else false
     */
    @Override
    public boolean containsDescripcion(String descripcion, String categoria) {
        try {
            return this.findByDescripcion(descripcion, categoria).size() > 0;
        } catch (Exception ex) {
            //Exception Handler
        }
        return false;
    }

    /**
     *
     * @param entity
     */
    @Override
    public void add(Producto entity) throws Exception  {
        Connection c = null;
        PreparedStatement pstmt = null;

        try {
            c = DBUtils.getConnection();
            pstmt = c.prepareStatement("INSERT INTO producto (descripcion, "
                    + "id_categoria, precio_unit, cantidad) "
                    + "values (?, ?, ?, ?)");

            pstmt.setString(1, entity.getDescripcion());
            pstmt.setInt(2, entity.getIdCategoria());
            pstmt.setLong(3, entity.getPrecioUnit());
            pstmt.setLong(4, entity.getCantidad());;

            pstmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                DBUtils.closeConnection(c);
            } catch (SQLException ex) {
                //Logger.getLogger(UsuarioManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     *
     * @param id
     */
    @Override
    public void remove(Integer id) throws Exception {
        Connection c = null;
        PreparedStatement pstmt = null;

        try {
            c = DBUtils.getConnection();
            pstmt = c.prepareStatement("DELETE FROM producto WHERE id_producto = ?");

            pstmt.setInt(1, id);

            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                DBUtils.closeConnection(c);
            } catch (SQLException ex) {
                //Logger.getLogger(UsuarioManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     *
     * @param entity
     */
    @Override
    public void update(Producto entity) throws Exception {
        Connection c = null;
        PreparedStatement pstmt = null;

        try {
            c = DBUtils.getConnection();
            pstmt = c.prepareStatement("UPDATE producto "
                    + "SET descripcion = ?, "
                    + "id_categoria = ?, "
                    + "precio_unit = ?, "
                    + "cantidad = ?"
                    + "WHERE id_producto = ?");

            pstmt.setString(1, entity.getDescripcion());
            pstmt.setInt(2, entity.getIdCategoria());
            pstmt.setLong(3, entity.getPrecioUnit());
            pstmt.setLong(4, entity.getCantidad());;
            pstmt.setInt(5, entity.getId());

            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                DBUtils.closeConnection(c);
            } catch (SQLException ex) {
                //Logger.getLogger(UsuarioManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public boolean contains(Integer id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public Entity get(Integer id) throws Exception {
        Entity retValue = null;

        Connection c = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            c = DBUtils.getConnection();
            pstmt = c.prepareStatement("SELECT * FROM producto WHERE id_producto = ?");

            pstmt.setInt(1, id);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                retValue = new Producto(rs.getInt("id_producto"), 
                        rs.getString("descripcion"), 
                        rs.getInt("id_categoria"), 
                        rs.getLong("precio_unit"), 
                        rs.getLong("cantidad"));
            } else {
                retValue = new Producto(null, null, 0, null, null);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                DBUtils.closeConnection(c);
            } catch (SQLException ex) {
                //Logger.getLogger(UsuarioManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return retValue;
    }

    /**
     *
     * @return
     */
    @Override
    public Collection<Producto> getAll() throws Exception {
        Collection<Producto> retValue = new ArrayList();

        Connection c = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            c = DBUtils.getConnection();
            pstmt = c.prepareStatement("SELECT * FROM producto order by descripcion");

            rs = pstmt.executeQuery();

            while (rs.next()) {
                retValue.add(new Producto(rs.getInt("id_producto"), 
                        rs.getString("descripcion"), 
                        rs.getInt("id_categoria"), 
                        rs.getLong("precio_unit"), 
                        rs.getLong("cantidad")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                DBUtils.closeConnection(c);
            } catch (SQLException ex) {
                //Logger.getLogger(UsuarioManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return retValue;
    }

    /**
     *
     * @param categoriaDescrip
     * @return
     * @throws Exception
     */
    @Override
    public Collection<Producto> findByDescripcion(String descripcion, String categoriaDescrip) throws Exception {
        Collection<Producto> retValue = new ArrayList();

        Connection c = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            String query;
            c = DBUtils.getConnection();
            if((categoriaDescrip == null || "null".equals(categoriaDescrip) || categoriaDescrip.equals("")) && !(descripcion == null || "null".equals(descripcion) || descripcion.equals(""))) {
                query = "SELECT * FROM producto WHERE descripcion like lower('%" + descripcion + "%') order by descripcion;";
            } else if((descripcion == null || "null".equals(descripcion) || descripcion.equals("")) && !((categoriaDescrip == null || "null".equals(categoriaDescrip) || categoriaDescrip.equals("")))) {
                query = "select p.* from public.producto p join " +
                        "public.categoria c " +
                        "on p.id_categoria = c.id_categoria " +
                        "where c.descripcion like lower('%" + categoriaDescrip + "%') order by p.descripcion;";;
            } else if((descripcion == null || "null".equals(descripcion) || descripcion.equals("")) && ((categoriaDescrip == null || "null".equals(categoriaDescrip) || categoriaDescrip.equals("")))) {
                query = "SELECT * FROM producto WHERE descripcion order by descripcion;";
            } else {
                query = "select p.* from public.producto p join " +
                        "public.categoria c " +
                        "on p.id_categoria = c.id_categoria " +
                        "where p.descripcion like lower('%" + descripcion + "%')and " +
                        "c.descripcion like lower('%" + categoriaDescrip + "%') order by p.descripcion;";
            }
            pstmt = c.prepareStatement(query);
            rs = pstmt.executeQuery();
            while(rs.next()) {
                retValue.add(
                    new Producto(rs.getInt("id_producto"), 
                    rs.getString("descripcion"), 
                    rs.getInt("id_categoria"), 
                    rs.getLong("precio_unit"), 
                    rs.getLong("cantidad"))
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                DBUtils.closeConnection(c);
            } catch (SQLException ex) {
                //Logger.getLogger(UsuarioManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return retValue;
    }

}
