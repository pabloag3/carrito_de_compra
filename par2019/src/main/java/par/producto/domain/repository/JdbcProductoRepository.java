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
    public boolean containsDescripcion(String descripcion) {
        try {
            return this.findByDescripcion(descripcion).size() > 0;
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
    public void add(Producto entity) {
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
    public void remove(Integer id) {
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
    public void update(Producto entity) {
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
    public Entity get(Integer id) {
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
    public Collection<Producto> getAll() {
        Collection<Producto> retValue = new ArrayList();

        Connection c = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            c = DBUtils.getConnection();
            pstmt = c.prepareStatement("SELECT * FROM producto");

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
     * @param nombre
     * @param apellido
     * @return
     * @throws Exception
     */
    @Override
    public Collection<Producto> findByDescripcion(String descripcion) throws Exception {
        Collection<Producto> retValue = new ArrayList();

        Connection c = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            c = DBUtils.getConnection();
            pstmt = c.prepareStatement("SELECT * FROM producto WHERE descripcion = ?");

            pstmt.setString(1, descripcion);

            rs = pstmt.executeQuery();

            if (rs.next()) {
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

}
