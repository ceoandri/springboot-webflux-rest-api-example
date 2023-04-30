package gratis.contoh.api.util.pagination;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Pagination {
	
	public static boolean isLastPage(int size, int offset, long totalData){
        return (totalData - size) <= offset;
    }

    public static boolean isFirstPage(int offset){
        return offset == 0;
    }

    public static int getTotalPages(int size, long totalData){
        return (int) Math.ceil(totalData/ (float)size);
    }
	
	public static Pageable getPageable(Object obj) {
		Gson gson = new GsonBuilder()
			    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
			    .create();		
		PaginationRequest res = new PaginationRequest();
		Sort sort = null;
		try {
			res = gson.fromJson(gson.toJson(obj), PaginationRequest.class);
			
			if (res.getPage() == null) {
				res.setPage(1);
			}
			
			if (res.getSize() == null) {
				res.setSize(Integer.MAX_VALUE);
			}
			
			if (res.getSort() == null || res.getSort().equals("")) {
				res.setSort("id");
			}
			
			String[] sortArr = res.getSort().split(" ");
			sort = Sort.by(sortArr[0]).ascending();
			
			if (sortArr.length > 1) {
				if (sortArr[1].equals("desc")) {
					sort = Sort.by(sortArr[0]).descending();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return PageRequest.of(res.getPage() - 1, res.getSize(), sort);
	}
	
	public static Pageable getPageable(Object obj, String defaultSort) {
		Gson gson = new GsonBuilder()
			    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
			    .create();
		PaginationRequest res = new PaginationRequest();
		Sort sort = null;
		try {
			res = gson.fromJson(gson.toJson(obj), PaginationRequest.class);
			
			if (res.getPage() == null) {
				res.setPage(1);
			}
			
			if (res.getSize() == null) {
				res.setSize(Integer.MAX_VALUE);
			}
			
			if (res.getSort() == null || res.getSort().equals("")) {
				res.setSort(defaultSort);
			}
			
			String[] sortArr = res.getSort().split(" ");
			sort = Sort.by(sortArr[0]).ascending();
			
			if (sortArr.length > 1) {
				if (sortArr[1].equals("desc")) {
					sort = Sort.by(sortArr[0]).descending();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return PageRequest.of(res.getPage() - 1, res.getSize(), sort);
	}
	
	public static PaginationRequest getPaginationRequest(Object obj) {
		Gson gson = new GsonBuilder()
			    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
			    .create();
		PaginationRequest res = new PaginationRequest();
		try {
			res = gson.fromJson(gson.toJson(obj), PaginationRequest.class);
			
			if (res.getPage() == null) {
				res.setPage(1);
			}
			
			if (res.getSize() == null) {
				res.setSize(Integer.MAX_VALUE);
			}
			
			if (res.getSort() == null || res.getSort().equals("")) {
				res.setSort("id");
			}
		} catch (Exception e) {
			
		}
		
		return res;
	}
	
	public static PaginationRequest getPaginationRequest(Object obj, String defaultSort) {
		Gson gson = new GsonBuilder()
			    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
			    .create();
		PaginationRequest res = new PaginationRequest();
		try {
			res = gson.fromJson(gson.toJson(obj), PaginationRequest.class);
			
			if (res.getPage() == null) {
				res.setPage(1);
			}
			
			if (res.getSize() == null) {
				res.setSize(Integer.MAX_VALUE);
			}
			
			if (res.getSort() == null || res.getSort().equals("")) {
				res.setSort(defaultSort);
			}
		} catch (Exception e) {
			
		}
		
		return res;
	}
	
}
