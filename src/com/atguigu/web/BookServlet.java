package com.atguigu.web;

import com.atguigu.pojo.Book;
import com.atguigu.pojo.Page;
import com.atguigu.service.BookService;
import com.atguigu.service.impl.BookServiceImpl;
import com.atguigu.utils.WebUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class BookServlet extends BaseServlet{
    private BookService bookService=new BookServiceImpl();

    protected void add(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int pageNo=WebUtils.parseInt(req.getParameter("pageNo"),0);
        pageNo+=1;
        Book book = WebUtils.copyParamToBean(req.getParameterMap(), new Book());

        bookService.addBook(book);

//        req.getRequestDispatcher("/manager/bookServlet?action=list").forward(req,resp);
        resp.sendRedirect(req.getContextPath()+"/manager/bookServlet?action=page&pageNo="+pageNo);
    }

    protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id=WebUtils.parseInt(req.getParameter("id"),0);
        bookService.deteBookById(id);
        resp.sendRedirect(req.getContextPath()+"/manager/bookServlet?action=page&pageNo="+req.getParameter("pageNo"));
    }

    protected void update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Book book=WebUtils.copyParamToBean(req.getParameterMap(),new Book());
        bookService.updateBook(book);
        resp.sendRedirect(req.getContextPath()+"/manager/bookServlet?action=page&pageNo="+req.getParameter("pageNo"));
    }

    protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Book> books =bookService.queryBooks();
        req.setAttribute("books",books);
        req.getRequestDispatcher("/pages/manager/book_manager.jsp").forward(req,resp);
    }

    protected void getBook(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id=WebUtils.parseInt(req.getParameter("id"),0);
        Book book=bookService.queryBookById(id);
        req.setAttribute("book",book);
        req.getRequestDispatcher("/pages/manager/book_edit.jsp").forward(req,resp);
    }

    /**
     * 处理分页功能
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    protected void page(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1 获取请求的参数pageNo 和pageSize
        int pageNo=WebUtils.parseInt(req.getParameter("pageNo"),1);
        int pageSize=WebUtils.parseInt(req.getParameter("pageSize"), Page.PAGE_SIZE);

        //2 调用Bookservice.page(pageNo，pageSize):page对象
        Page<Book> page=bookService.page(pageNo,pageSize);
        page.setUrl("manager/bookServlet?action=page");
        //3保存Page对象到Request域中
        req.setAttribute("page",page);

        //4 请求转发到pages/manager/book_manager.jsp页面
        req.getRequestDispatcher("/pages/manager/book_manager.jsp").forward(req,resp);
    }

}
